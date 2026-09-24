
SELECT consultation_id    as "ID consultation",
       titre_consultation AS "Titre consultation",
       question_id        AS "ID question",
       question_titre     AS "Titre question",
       id                 AS "Choice ID",
       label              AS "Choice label"
FROM components_question_choixes
         LEFT JOIN (SELECT CONCAT(components_question_question_a_choix_multiples_components.component_id,
                                  components_question_question_a_choix_uniques_components.component_id,
                                  components_question_question_conditionnelles_components.component_id) AS "component_id",
                           CONCAT(components_question_question_a_choix_multiples_components.entity_id,
                                  components_question_question_a_choix_uniques_components.entity_id,
                                  components_question_question_conditionnelles_components.entity_id)    AS "entity_id",
                           CONCAT(
                                   components_question_question_a_choix_multiples.titre,
                                   components_question_question_a_choix_uniques.titre,
                                   components_question_question_conditionnelles.titre
                           )                                                                            AS "question_titre",
                           CONCAT(
                                   components_question_question_a_choix_multiples.id,
                                   components_question_question_a_choix_uniques.id,
                                   components_question_question_conditionnelles.id
                           )                                                                            AS "question_id",
                           consultations.titre_consultation,
                           consultations.id                                                             AS "consultation_id"
                    FROM consultations
                             LEFT JOIN consultations_components ON consultations_components.entity_id = consultations.id

                             LEFT JOIN components_question_question_a_choix_multiples
                                       ON consultations_components.component_id =
                                          components_question_question_a_choix_multiples.id
                             LEFT JOIN components_question_question_a_choix_uniques
                                       ON consultations_components.component_id =
                                          components_question_question_a_choix_uniques.id
                             LEFT JOIN components_question_question_conditionnelles
                                       ON consultations_components.component_id =
                                          components_question_question_conditionnelles.id

                             LEFT JOIN components_question_question_a_choix_multiples_components
                                       ON components_question_question_a_choix_multiples_components.entity_id =
                                          components_question_question_a_choix_multiples.id
                             LEFT JOIN components_question_question_a_choix_uniques_components
                                       ON components_question_question_a_choix_uniques_components.entity_id =
                                          components_question_question_a_choix_uniques.id
                             LEFT JOIN components_question_question_conditionnelles_components
                                       ON components_question_question_conditionnelles_components.entity_id =
                                          components_question_question_conditionnelles.id
                    WHERE consultations_components.component_type = 'question-de-consultation.question-a-choix-unique'
                       OR
                        consultations_components.component_type = 'question-de-consultation.question-a-choix-multiples'
                       OR consultations_components.component_type =
                          'question-de-consultation.question-conditionnelle') component
                   ON component.component_id = components_question_choixes.id::text

WHERE TRUE
    [[AND consultation_id = {{consultation_id}}]]
    [[AND question_id = {{question_id}}]]

ORDER BY consultation_id, question_id, "Choice ID";
