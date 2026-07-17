-- Migration ACME : ajout de la colonne status dans acme_certificate
-- Date : 2026-07-07
-- Objectif : permettre l'idempotence du traitement journalier de renouvellement
--            en traçant si le certificat a été déployé avec succès sur Cloudflare.
-- Valeurs possibles : 'TO_DEPLOY' (déployé en attente) | 'DEPLOYED' (déployé avec succès)

ALTER TABLE acme_certificate
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'TO_DEPLOY';

-- Les certificats déjà présents en base sont supposés avoir été déployés avec succès.
UPDATE acme_certificate SET status = 'DEPLOYED';
