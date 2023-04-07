package fr.social.gouv.agora.domain

import java.util.*

data class Consultation(
    val id: String,
    val title: String,
    val abstract: String,
    val start_date: Date?,
    val end_date: Date,
    val cover_url: String,
    val question_count: String,
    val estimated_time: String,
    val participant_count_goal: Int,
    val description: String,
    val tips_description: String,
    val id_thematique: String,
)