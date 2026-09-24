SELECT
    CONCAT(
            components_question_question_a_choix_multiples.id,
            components_question_question_a_choix_uniques.id,
            components_question_question_conditionnelles.id,
            components_question_question_ouvertes.id
    ) AS "ID",
    consultations_components.order AS "Numéro",
    SPLIT_PART(consultations_components.component_type, '.', 2) AS "Type",
    CONCAT(
            components_question_question_a_choix_multiples.titre,
            components_question_question_a_choix_uniques.titre,
            components_question_question_conditionnelles.titre,
            components_question_question_ouvertes.titre
    ) AS "Intitulé"
FROM consultations_components

         LEFT JOIN components_question_question_a_choix_multiples ON consultations_components.component_id = components_question_question_a_choix_multiples.id
         LEFT JOIN components_question_question_a_choix_uniques ON consultations_components.component_id = components_question_question_a_choix_uniques.id
         LEFT JOIN components_question_question_conditionnelles ON consultations_components.component_id = components_question_question_conditionnelles.id
         LEFT JOIN components_question_question_ouvertes ON consultations_components.component_id = components_question_question_ouvertes.id

WHERE TRUE
    [[AND consultations_components.entity_id = {{consultation_id}}]]
    [[AND consultations_components.component_id = {{component_id}}]]
ORDER BY consultations_components.order;
