#!/usr/bin/env node
/**
 * Génère le script SQL de migration des question_id et choice_id
 * pour toutes les consultations (consultation_results + reponses_consultation).
 *
 * Stratégie :
 *  1. Jointure question : (consultation_old_id == consultation_new_internal_id) + titre + type
 *  2. Jointure choix    : (new_question_id) + label  → unique au sein d'une question
 */

const fs = require('fs');
const path = require('path');

const DIR = path.dirname(__filename);

// ── Chargement des fichiers ───────────────────────────────────────────────────
const v4Questions = JSON.parse(fs.readFileSync(path.join(DIR, 'strapi_v4_questions.json'), 'utf8'));
const v5Questions = JSON.parse(fs.readFileSync(path.join(DIR, 'strapi_v5_questions.json'), 'utf8'));
const v4Choices   = JSON.parse(fs.readFileSync(path.join(DIR, 'strapi_v4_choices.json'),   'utf8'));
const v5Choices   = JSON.parse(fs.readFileSync(path.join(DIR, 'strapi_v5_choices.json'),   'utf8'));

// ── Étape préliminaire : mapper consultation_old_id → consultation_document_id ──
// On relie les consultations par les titres de leurs questions.
// Pour chaque consultation v4, on cherche la consultation v5 qui a le plus de titres de questions en commun.

// Index v5 : titre+type → liste de { consultation_new_internal_id, consultation_document_id, new_question_id }
const v5ByTitleType = new Map(); // "titre|type" → [{consultation_new_internal_id, consultation_document_id, new_question_id}]
for (const q of v5Questions) {
  const key = `${q.question_titre.trim()}|${q.question_type}`;
  if (!v5ByTitleType.has(key)) v5ByTitleType.set(key, []);
  v5ByTitleType.get(key).push(q);
}

// Pour chaque consultation v4, compter les votes pour chaque consultation v5
const consultationMapping = new Map(); // consultation_old_id → { consultation_new_internal_id, consultation_document_id }
const v4ConsultIds = [...new Set(v4Questions.map(q => q.consultation_old_id))];

for (const oldId of v4ConsultIds) {
  const questionsForConsult = v4Questions.filter(q => q.consultation_old_id === oldId);
  const votes = new Map(); // "newInternalId|docId" → count

  for (const q4 of questionsForConsult) {
    const key = `${q4.question_titre.trim()}|${q4.question_type}`;
    const candidates = v5ByTitleType.get(key) || [];
    for (const c of candidates) {
      const voteKey = `${c.consultation_new_internal_id}|${c.consultation_document_id}`;
      votes.set(voteKey, (votes.get(voteKey) || 0) + 1);
    }
  }

  if (votes.size === 0) {
    console.warn(`⚠️  Consultation v4 id=${oldId} : aucun match v5 trouvé`);
    continue;
  }

  // Prendre le meilleur match (le plus de questions en commun)
  // En cas d'égalité, prendre le consultation_new_internal_id le plus élevé
  // (car Strapi v5 a dupliqué les consultations : la plus récente a les nouveaux IDs)
  let best = null, bestCount = 0, bestInternalId = -1;
  for (const [voteKey, count] of votes) {
    const [internalIdStr] = voteKey.split('|');
    const internalId = parseInt(internalIdStr);
    if (count > bestCount || (count === bestCount && internalId > bestInternalId)) {
      bestCount = count;
      bestInternalId = internalId;
      best = voteKey;
    }
  }

  const [newInternalIdStr, docId] = best.split('|');
  consultationMapping.set(oldId, {
    consultation_new_internal_id: parseInt(newInternalIdStr),
    consultation_document_id: docId,
    matched_questions: bestCount,
    total_questions: questionsForConsult.length,
  });
}

// Log du mapping consultations
console.log('\n📋 Mapping consultations :');
for (const [oldId, m] of consultationMapping) {
  const ok = m.matched_questions === m.total_questions ? '✅' : '⚠️ ';
  console.log(`   ${ok} v4 id=${oldId} → v5 internal=${m.consultation_new_internal_id} / docId=${m.consultation_document_id} (${m.matched_questions}/${m.total_questions} questions matchées)`);
}
console.log('');

// ── Index v5 questions : (consultation_new_internal_id + titre + type) → new_question_id ──
const v5QuestionIndex = new Map();
for (const q of v5Questions) {
  const key = `${q.consultation_new_internal_id}|${q.question_titre.trim()}|${q.question_type}`;
  if (!v5QuestionIndex.has(key)) {
    v5QuestionIndex.set(key, q);
  }
}

// ── Index v5 choix : new_question_id + label → new_choice_id ─────────────────
const v5ChoiceIndex = new Map();
const v5ChoiceDuplicates = new Set();
for (const c of v5Choices) {
  const key = `${c.new_question_id}|${c.choice_label.trim()}`;
  if (v5ChoiceIndex.has(key)) {
    v5ChoiceDuplicates.add(key);
  } else {
    v5ChoiceIndex.set(key, c.new_choice_id);
  }
}

// ── Index v5 choices par new_question_id pour récup la consultation ───────────
// On a besoin du document_id pour les UPDATE (la colonne consultation_id du back)
const v5QuestionToDocumentId = new Map();
for (const q of v5Questions) {
  v5QuestionToDocumentId.set(q.new_question_id, q.consultation_document_id);
}

// ── Construction du mapping question old → new ────────────────────────────────
// Map: old_question_id → { new_question_id, consultation_document_id }
const questionMapping = new Map(); // old_question_id → { new_question_id, consultation_document_id }
const unmatchedQuestions = [];

for (const q4 of v4Questions) {
  const cm = consultationMapping.get(q4.consultation_old_id);
  if (!cm) {
    unmatchedQuestions.push(q4);
    continue;
  }
  const key = `${cm.consultation_new_internal_id}|${q4.question_titre.trim()}|${q4.question_type}`;
  const v5q = v5QuestionIndex.get(key);
  if (v5q) {
    questionMapping.set(q4.old_question_id, {
      new_question_id: v5q.new_question_id,
      consultation_document_id: v5q.consultation_document_id,
    });
  } else {
    unmatchedQuestions.push(q4);
  }
}

// ── Construction du mapping choix old → new ───────────────────────────────────
// On itère sur les choix v4, on trouve le new_question_id via questionMapping,
// puis on cherche le choix v5 par (new_question_id + label)
const choiceMapping = new Map(); // old_choice_id → { new_choice_id, old_question_id, new_question_id, consultation_document_id }
const unmatchedChoices = [];
const ambiguousChoices = [];

for (const c4 of v4Choices) {
  const qMap = questionMapping.get(c4.old_question_id);
  if (!qMap) {
    // Question non mappée → choix non mappé aussi
    continue;
  }
  const { new_question_id, consultation_document_id } = qMap;
  const choiceKey = `${new_question_id}|${c4.choice_label.trim()}`;

  if (v5ChoiceDuplicates.has(choiceKey)) {
    ambiguousChoices.push({ ...c4, new_question_id, consultation_document_id });
    continue;
  }

  const new_choice_id = v5ChoiceIndex.get(choiceKey);
  if (new_choice_id !== undefined) {
    choiceMapping.set(c4.old_choice_id, {
      new_choice_id,
      old_question_id: c4.old_question_id,
      new_question_id,
      consultation_document_id,
    });
  } else {
    unmatchedChoices.push({ ...c4, new_question_id, consultation_document_id });
  }
}

// ── Génération du SQL ─────────────────────────────────────────────────────────
const lines = [];

lines.push(`-- ============================================================`);
lines.push(`-- Migration automatique question_id / choice_id`);
lines.push(`-- Généré le ${new Date().toISOString()}`);
lines.push(`-- Script : generate_migration_sql.js`);
lines.push(`-- ============================================================`);
lines.push(``);
lines.push(`-- Questions mappées     : ${questionMapping.size} / ${v4Questions.length}`);
lines.push(`-- Questions non mappées : ${unmatchedQuestions.length}`);
lines.push(`-- Choix mappés          : ${choiceMapping.size} / ${v4Choices.length}`);
lines.push(`-- Choix non mappés      : ${unmatchedChoices.length}`);
lines.push(`-- Choix ambigus (label dupliqué dans même question) : ${ambiguousChoices.length}`);
lines.push(``);

// ── ÉTAPE 1 : UPDATE question_id ─────────────────────────────────────────────
lines.push(`-- ============================================================`);
lines.push(`-- ÉTAPE 1 : Mise à jour des question_id`);
lines.push(`-- ============================================================`);
lines.push(``);

// Grouper par consultation_document_id pour la lisibilité
const questionsByConsultation = new Map();
for (const [old_question_id, mapping] of questionMapping) {
  const { consultation_document_id, new_question_id } = mapping;
  if (old_question_id === new_question_id) continue; // rien à faire
  if (!questionsByConsultation.has(consultation_document_id)) {
    questionsByConsultation.set(consultation_document_id, []);
  }
  questionsByConsultation.get(consultation_document_id).push({ old_question_id, new_question_id });
}

for (const [doc_id, mappings] of questionsByConsultation) {
  lines.push(`-- Consultation ${doc_id}`);
  for (const { old_question_id, new_question_id } of mappings) {
    lines.push(`UPDATE consultation_results SET question_id = '${new_question_id}' WHERE question_id = '${old_question_id}' AND consultation_id = '${doc_id}';`);
    lines.push(`UPDATE reponses_consultation SET question_id = '${new_question_id}' WHERE question_id = '${old_question_id}' AND consultation_id = '${doc_id}';`);
  }
  lines.push(``);
}

// ── ÉTAPE 2 : UPDATE choice_id ───────────────────────────────────────────────
lines.push(`-- ============================================================`);
lines.push(`-- ÉTAPE 2 : Mise à jour des choice_id`);
lines.push(`-- ============================================================`);
lines.push(``);

// Grouper par consultation + question
const choicesByConsultationQuestion = new Map();
for (const [old_choice_id, mapping] of choiceMapping) {
  const { new_choice_id, old_question_id, new_question_id, consultation_document_id } = mapping;
  if (old_choice_id === new_choice_id) continue; // rien à faire
  const groupKey = `${consultation_document_id}|${new_question_id}`;
  if (!choicesByConsultationQuestion.has(groupKey)) {
    choicesByConsultationQuestion.set(groupKey, { consultation_document_id, new_question_id, choices: [] });
  }
  choicesByConsultationQuestion.get(groupKey).choices.push({ old_choice_id, new_choice_id });
}

for (const [, group] of choicesByConsultationQuestion) {
  const { consultation_document_id, new_question_id, choices } = group;
  lines.push(`-- Consultation ${consultation_document_id} / question ${new_question_id}`);
  for (const { old_choice_id, new_choice_id } of choices) {
    lines.push(`UPDATE consultation_results SET choice_id = '${new_choice_id}' WHERE choice_id = '${old_choice_id}' AND question_id = '${new_question_id}' AND consultation_id = '${consultation_document_id}';`);
    lines.push(`UPDATE reponses_consultation SET choice_id = '${new_choice_id}' WHERE choice_id = '${old_choice_id}' AND question_id = '${new_question_id}' AND consultation_id = '${consultation_document_id}';`);
  }
  lines.push(``);
}

// ── WARNINGS ─────────────────────────────────────────────────────────────────
if (unmatchedQuestions.length > 0) {
  lines.push(`-- ============================================================`);
  lines.push(`-- ⚠️  QUESTIONS NON MAPPÉES (à traiter manuellement)`);
  lines.push(`-- ============================================================`);
  for (const q of unmatchedQuestions) {
    lines.push(`-- consultation_old_id=${q.consultation_old_id} | old_question_id=${q.old_question_id} | type=${q.question_type} | titre="${q.question_titre.trim()}"`);
  }
  lines.push(``);
}

if (unmatchedChoices.length > 0) {
  lines.push(`-- ============================================================`);
  lines.push(`-- ⚠️  CHOIX NON MAPPÉS (à traiter manuellement)`);
  lines.push(`-- ============================================================`);
  for (const c of unmatchedChoices) {
    lines.push(`-- old_question_id=${c.old_question_id} | new_question_id=${c.new_question_id} | old_choice_id=${c.old_choice_id} | label="${c.choice_label.trim()}"`);
  }
  lines.push(``);
}

if (ambiguousChoices.length > 0) {
  lines.push(`-- ============================================================`);
  lines.push(`-- ⚠️  CHOIX AMBIGUS (label dupliqué dans la même question v5)`);
  lines.push(`-- ============================================================`);
  for (const c of ambiguousChoices) {
    lines.push(`-- old_question_id=${c.old_question_id} | new_question_id=${c.new_question_id} | old_choice_id=${c.old_choice_id} | label="${c.choice_label.trim()}"`);
  }
  lines.push(``);
}

lines.push(`-- ============================================================`);
lines.push(`-- APRÈS EXÉCUTION : invalider le cache Redis`);
lines.push(`-- redis-cli KEYS "consultation_results_*" | xargs redis-cli DEL`);
lines.push(`-- ============================================================`);

// ── Écriture du fichier SQL ───────────────────────────────────────────────────
const outputPath = path.join(DIR, 'mig_all_question_choice_ids.sql');
fs.writeFileSync(outputPath, lines.join('\n'), 'utf8');

console.log(`✅ SQL généré : ${outputPath}`);
console.log(`   Questions mappées     : ${questionMapping.size} / ${v4Questions.length}`);
console.log(`   Questions non mappées : ${unmatchedQuestions.length}`);
console.log(`   Choix mappés          : ${choiceMapping.size} / ${v4Choices.length}`);
console.log(`   Choix non mappés      : ${unmatchedChoices.length}`);
console.log(`   Choix ambigus         : ${ambiguousChoices.length}`);

if (unmatchedQuestions.length > 0) {
  console.log(`\n⚠️  Questions non mappées :`);
  for (const q of unmatchedQuestions) {
    console.log(`   - consultation_old_id=${q.consultation_old_id} | id=${q.old_question_id} | "${q.question_titre.trim()}"`);
  }
}
