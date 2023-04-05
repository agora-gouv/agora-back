-- SCHEMA: SCH_AGORA

-- DROP SCHEMA IF EXISTS "SCH_AGORA" ;

CREATE SCHEMA IF NOT EXISTS "SCH_AGORA"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "SCH_AGORA" TO backend;

GRANT ALL ON SCHEMA "SCH_AGORA" TO postgres;

-- Table: SCH_AGORA.thematiques

-- DROP TABLE IF EXISTS "SCH_AGORA".thematiques;

CREATE TABLE IF NOT EXISTS "SCH_AGORA".thematiques
(
    id integer PRIMARY KEY,
    label text,
    picto text,
    color text

);

ALTER TABLE IF EXISTS "SCH_AGORA".thematiques
    OWNER to postgres;

GRANT ALL ON TABLE "SCH_AGORA".thematiques TO backend;

GRANT ALL ON TABLE "SCH_AGORA".thematiques TO postgres;