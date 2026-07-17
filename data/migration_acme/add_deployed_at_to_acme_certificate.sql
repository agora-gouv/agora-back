-- Migration : ajout du champ deployed_at sur acme_certificate
-- Permet de tracer précisément quand chaque certificat a été déployé sur Cloudflare
-- Les lignes existantes auront deployed_at = NULL (certificats déployés avant cette migration)

ALTER TABLE acme_certificate ADD COLUMN deployed_at TIMESTAMP;
