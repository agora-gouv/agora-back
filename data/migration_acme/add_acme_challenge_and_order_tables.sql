-- Migration ACME : ajout des tables acme_challenge et acme_order
-- Date : 2026-07-07
-- Objectif :
--   1. acme_challenge : persistance du token HTTP-01 en base de données partagée,
--      accessible par toutes les instances Scalingo (remplace le ConcurrentHashMap en mémoire).
--   2. acme_order : persistance de l'état d'une commande ACME en cours,
--      permettant la reprise sur incident (redémarrage, crash) sans perdre le contexte.

-- Table pour les tokens ACME HTTP-01
-- Le token est la clé primaire car il est unique et utilisé pour les lookups.
CREATE TABLE acme_challenge (
    token             VARCHAR(255) PRIMARY KEY,
    key_authorization TEXT NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT now()
);

-- Table pour les orders ACME en cours
-- Statuts possibles :
--   CHALLENGE_PENDING : challenge HTTP-01 déclenché, polling Sectigo en attente
--   ORDER_FINALIZING  : challenge validé, CSR envoyé, polling final en attente
CREATE TABLE acme_order (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    domain         VARCHAR(255) NOT NULL,
    order_url      VARCHAR(500) NOT NULL,   -- URL de l'order ACME pour reprendre le polling
    domain_key_pem TEXT NOT NULL,           -- clé privée domaine (chiffrée AES-256-GCM)
    status         VARCHAR(50)  NOT NULL,   -- CHALLENGE_PENDING | ORDER_FINALIZING
    created_at     TIMESTAMP    NOT NULL DEFAULT now()
);
