#!/usr/bin/env node
/**
 * Génère le script SQL de ROLLBACK de la migration question_id / choice_id.
 *
 * Il parse le fichier mig_all_question_choice_ids.sql et inverse toutes les UPDATE
 * en respectant l'ordre critique :
 *
 *   1. D'abord rollback ÉTAPE 2 (choice_id) — car les WHERE utilisent les NOUVEAUX question_id
 *   2. Ensuite rollback ÉTAPE 1 (question_id) — on remet les anciens question_id
 *
 * Usage :
 *   node generate_rollback_sql.js
 */

const fs   = require('fs');
const path = require('path');

const DIR          = path.dirname(__filename);
const INPUT_FILE   = path.join(DIR, 'mig_all_question_choice_ids.sql');
const OUTPUT_FILE  = path.join(DIR, 'rollback_mig_all_question_choice_ids.sql');

// ── Lecture du fichier de migration ──────────────────────────────────────────
if (!fs.existsSync(INPUT_FILE)) {
  console.error(`❌ Fichier introuvable : ${INPUT_FILE}`);
  process.exit(1);
}

const content = fs.readFileSync(INPUT_FILE, 'utf8');
const allLines = content.split('\n');

// ── Détection du point de séparation entre ÉTAPE 1 et ÉTAPE 2 ────────────────
// On repère la ligne de commentaire "ÉTAPE 2"
let step2StartIndex = -1;
for (let i = 0; i < allLines.length; i++) {
  if (allLines[i].includes('ÉTAPE 2')) {
    step2StartIndex = i;
    break;
  }
}

if (step2StartIndex === -1) {
  console.error('❌ Impossible de trouver la séparation ÉTAPE 1 / ÉTAPE 2 dans le SQL.');
  process.exit(1);
}

const step1Lines = allLines.slice(0, step2StartIndex);
const step2Lines = allLines.slice(step2StartIndex);

console.log(`📄 Fichier source : ${INPUT_FILE}`);
console.log(`   Lignes ÉTAPE 1 : ${step1Lines.length}`);
console.log(`   Lignes ÉTAPE 2 : ${step2Lines.length}`);

// ── Regex pour extraire les UPDATE de migration ───────────────────────────────
// ÉTAPE 1 : UPDATE <table> SET question_id = 'NEW' WHERE question_id = 'OLD' AND consultation_id = 'CONSULT';
const REGEX_QUESTION = /^UPDATE\s+(\S+)\s+SET\s+question_id\s*=\s*'([^']+)'\s+WHERE\s+question_id\s*=\s*'([^']+)'\s+AND\s+consultation_id\s*=\s*'([^']+)';$/;

// ÉTAPE 2 : UPDATE <table> SET choice_id = 'NEW' WHERE choice_id = 'OLD' AND question_id = 'QNEW' AND consultation_id = 'CONSULT';
const REGEX_CHOICE = /^UPDATE\s+(\S+)\s+SET\s+choice_id\s*=\s*'([^']+)'\s+WHERE\s+choice_id\s*=\s*'([^']+)'\s+AND\s+question_id\s*=\s*'([^']+)'\s+AND\s+consultation_id\s*=\s*'([^']+)';$/;

// ── Extraction des UPDATE de l'ÉTAPE 1 (question_id) ─────────────────────────
// Structure : { table, consultation_id, old_question_id, new_question_id }
const questionUpdates = [];
let currentStep1Consultation = null;

for (const line of step1Lines) {
  const trimmed = line.trim();

  // Mémoriser le commentaire de consultation pour les blocs
  const consultComment = trimmed.match(/^--\s*Consultation\s+(\S+)$/);
  if (consultComment) {
    currentStep1Consultation = consultComment[1];
    continue;
  }

  const m = trimmed.match(REGEX_QUESTION);
  if (m) {
    const [, table, newQuestionId, oldQuestionId, consultationId] = m;
    questionUpdates.push({ table, consultationId, oldQuestionId, newQuestionId });
  }
}

// ── Extraction des UPDATE de l'ÉTAPE 2 (choice_id) ───────────────────────────
// Structure : { table, consultation_id, new_question_id, old_choice_id, new_choice_id }
const choiceUpdates = [];

for (const line of step2Lines) {
  const trimmed = line.trim();
  const m = trimmed.match(REGEX_CHOICE);
  if (m) {
    const [, table, newChoiceId, oldChoiceId, newQuestionId, consultationId] = m;
    choiceUpdates.push({ table, consultationId, newQuestionId, oldChoiceId, newChoiceId });
  }
}

console.log(`\n🔍 UPDATE analysés :`);
console.log(`   question_id updates : ${questionUpdates.length}`);
console.log(`   choice_id updates   : ${choiceUpdates.length}`);

// ── Génération des lignes SQL de rollback ─────────────────────────────────────
const lines = [];

lines.push(`-- ============================================================`);
lines.push(`-- ROLLBACK de la migration question_id / choice_id`);
lines.push(`-- Généré le ${new Date().toISOString()}`);
lines.push(`-- Script source : mig_all_question_choice_ids.sql`);
lines.push(`-- ============================================================`);
lines.push(`--`);
lines.push(`-- ⚠️  ORDRE D'EXÉCUTION CRITIQUE :`);
lines.push(`--   1. D'abord rollback ÉTAPE 2 (choice_id) car les WHERE utilisent`);
lines.push(`--      les nouveaux question_id (déjà en base après l'ÉTAPE 1).`);
lines.push(`--   2. Ensuite rollback ÉTAPE 1 (question_id) pour remettre les anciens.`);
lines.push(`--`);
lines.push(`-- Questions à annuler : ${questionUpdates.length / 2} (${questionUpdates.length / 2} consultation_results + ${questionUpdates.length / 2} reponses_consultation)`);
lines.push(`-- Choix à annuler     : ${choiceUpdates.length / 2} (${choiceUpdates.length / 2} consultation_results + ${choiceUpdates.length / 2} reponses_consultation)`);
lines.push(`-- ============================================================`);
lines.push(``);

// ── ROLLBACK ÉTAPE 2 (choice_id) EN PREMIER ──────────────────────────────────
lines.push(`-- ============================================================`);
lines.push(`-- ROLLBACK ÉTAPE 2 : Restauration des anciens choice_id`);
lines.push(`-- (utilise les nouveaux question_id encore présents en base)`);
lines.push(`-- ============================================================`);
lines.push(``);

// Grouper par consultation + question pour la lisibilité
const choiceGroups = new Map(); // "consultationId|newQuestionId" → { consultationId, newQuestionId, rows: [] }
for (const u of choiceUpdates) {
  const key = `${u.consultationId}|${u.newQuestionId}`;
  if (!choiceGroups.has(key)) {
    choiceGroups.set(key, { consultationId: u.consultationId, newQuestionId: u.newQuestionId, rows: [] });
  }
  choiceGroups.get(key).rows.push(u);
}

// Dédupliquer les paires (table, consultationId, newQuestionId, oldChoiceId, newChoiceId)
// car chaque paire apparaît en double (consultation_results + reponses_consultation)
for (const [, group] of choiceGroups) {
  const { consultationId, newQuestionId, rows } = group;
  lines.push(`-- Consultation ${consultationId} / question ${newQuestionId}`);

  // Grouper par table pour émettre les deux tables ensemble
  const byTable = new Map();
  for (const row of rows) {
    if (!byTable.has(row.table)) byTable.set(row.table, []);
    byTable.get(row.table).push(row);
  }

  // On prend les paires uniques (old/new choice_id) à partir de consultation_results
  const consultationResultsRows = byTable.get('consultation_results') || rows.filter((_, i) => i % 2 === 0);
  for (const row of consultationResultsRows) {
    // Rollback : remettre l'ancien choice_id là où est le nouveau
    lines.push(`UPDATE consultation_results SET choice_id = '${row.oldChoiceId}' WHERE choice_id = '${row.newChoiceId}' AND question_id = '${row.newQuestionId}' AND consultation_id = '${row.consultationId}';`);
    lines.push(`UPDATE reponses_consultation SET choice_id = '${row.oldChoiceId}' WHERE choice_id = '${row.newChoiceId}' AND question_id = '${row.newQuestionId}' AND consultation_id = '${row.consultationId}';`);
  }
  lines.push(``);
}

// ── ROLLBACK ÉTAPE 1 (question_id) EN SECOND ─────────────────────────────────
lines.push(`-- ============================================================`);
lines.push(`-- ROLLBACK ÉTAPE 1 : Restauration des anciens question_id`);
lines.push(`-- (à exécuter APRÈS le rollback des choice_id ci-dessus)`);
lines.push(`-- ============================================================`);
lines.push(``);

// Grouper par consultation pour la lisibilité (en dédupliquant consultation_results/reponses)
const questionGroups = new Map(); // consultationId → rows[]
for (const u of questionUpdates) {
  if (!questionGroups.has(u.consultationId)) questionGroups.set(u.consultationId, []);
  questionGroups.get(u.consultationId).push(u);
}

for (const [consultationId, rows] of questionGroups) {
  lines.push(`-- Consultation ${consultationId}`);
  // Dédupliquer : on n'émet qu'une fois par (oldQuestionId, newQuestionId)
  const seen = new Set();
  for (const row of rows) {
    if (row.table !== 'consultation_results') continue; // on laisse le script émettre les deux
    const key = `${row.oldQuestionId}|${row.newQuestionId}`;
    if (seen.has(key)) continue;
    seen.add(key);
    // Rollback : remettre l'ancien question_id là où est le nouveau
    lines.push(`UPDATE consultation_results SET question_id = '${row.oldQuestionId}' WHERE question_id = '${row.newQuestionId}' AND consultation_id = '${row.consultationId}';`);
    lines.push(`UPDATE reponses_consultation SET question_id = '${row.oldQuestionId}' WHERE question_id = '${row.newQuestionId}' AND consultation_id = '${row.consultationId}';`);
  }
  lines.push(``);
}

lines.push(`-- ============================================================`);
lines.push(`-- APRÈS ROLLBACK : invalider le cache Redis`);
lines.push(`-- redis-cli KEYS "consultation_results_*" | xargs redis-cli DEL`);
lines.push(`-- ============================================================`);

// ── Écriture du fichier SQL de rollback ───────────────────────────────────────
fs.writeFileSync(OUTPUT_FILE, lines.join('\n'), 'utf8');

console.log(`\n✅ Script de rollback généré : ${OUTPUT_FILE}`);
console.log(`   choice_id à annuler    : ${choiceUpdates.filter(u => u.table === 'consultation_results').length}`);
console.log(`   question_id à annuler  : ${questionUpdates.filter(u => u.table === 'consultation_results').length}`);
