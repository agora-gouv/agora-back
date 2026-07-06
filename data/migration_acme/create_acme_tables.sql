-- Migration ACME : création des tables pour le renouvellement automatique de certificats TLS
-- Date : 2026-06-25
-- Note : spring.jpa.hibernate.ddl-auto=update crée les tables automatiquement via les entités JPA.
--        Ce script sert de référence documentaire et pour les opérations manuelles éventuelles.

CREATE TABLE acme_certificate (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    domain      VARCHAR(255) NOT NULL,
    certificate TEXT NOT NULL,       -- PEM chain complet (cert + intermédiaires)
    private_key TEXT NOT NULL,       -- PEM clé privée domaine (chiffrée AES-256-GCM)
    expires_at  TIMESTAMP NOT NULL,  -- date d'expiration extraite du certificat X.509
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE acme_account (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    server_url  VARCHAR(500) NOT NULL,    -- URL du répertoire ACME (pour retrouver le bon compte par CA)
    account_url VARCHAR(500),             -- URL du compte ACME enregistré (null avant 1ère création)
    key_pem     TEXT NOT NULL,            -- keypair RSA/EC du compte (chiffrée AES-256-GCM)
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);
