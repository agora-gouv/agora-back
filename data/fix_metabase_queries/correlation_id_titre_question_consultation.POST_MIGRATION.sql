SELECT
    MIN(CONCAT(
            components_question_question_a_choix_multiples.id,
            components_question_question_a_choix_uniques.id,
            components_question_question_conditionnelles.id,
            components_question_question_ouvertes.id
    )) FILTER (WHERE consultations.published_at IS NULL)     AS "ID draft",
    MIN(CONCAT(
            components_question_question_a_choix_multiples.id,
            components_question_question_a_choix_uniques.id,
            components_question_question_conditionnelles.id,
            components_question_question_ouvertes.id
    )) FILTER (WHERE consultations.published_at IS NOT NULL) AS "ID published",
    consultations_cmps.order AS "Numéro",
    SPLIT_PART(consultations_cmps.component_type, '.', 2) AS "Type",
    MIN(CONCAT(
            components_question_question_a_choix_multiples.titre,
            components_question_question_a_choix_uniques.titre,
            components_question_question_conditionnelles.titre,
            components_question_question_ouvertes.titre
    )) AS "Intitulé"
FROM consultations

         INNER JOIN consultations_cmps ON consultations_cmps.entity_id = consultations.id
         LEFT JOIN components_question_question_a_choix_multiples ON consultations_cmps.cmp_id = components_question_question_a_choix_multiples.id
         LEFT JOIN components_question_question_a_choix_uniques ON consultations_cmps.cmp_id = components_question_question_a_choix_uniques.id
         LEFT JOIN components_question_question_conditionnelles ON consultations_cmps.cmp_id = components_question_question_conditionnelles.id
         LEFT JOIN components_question_question_ouvertes ON consultations_cmps.cmp_id = components_question_question_ouvertes.id

WHERE TRUE
    [[AND consultations.document_id = {{consultation_id}}]]
    [[AND consultations_cmps.cmp_id = {{component_id}}]]
GROUP BY consultations.document_id, consultations_cmps.order, consultations_cmps.component_type
ORDER BY consultations_cmps.order;
