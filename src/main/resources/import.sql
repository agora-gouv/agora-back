-- SCHEMA: SCH_AGORA

-- DROP SCHEMA IF EXISTS "SCH_AGORA" ;
CREATE SCHEMA IF NOT EXISTS "SCH_AGORA";

-- Table: SCH_AGORA.thematiques

-- DROP TABLE IF EXISTS "SCH_AGORA".thematiques;
CREATE TABLE IF NOT EXISTS "SCH_AGORA".thematiques(id uuid PRIMARY KEY, label text, picto text, color text);
