package fr.social.gouv.agora.domain

data class Profile(
    val id: String,
    val gender: String,
    val yearOfBirth: Int,
    val department: String,
    val cityType: String,
    val jobCategory: String,
    val voteFrequency: String,
    val publicMeetingFrequency: String,
    val consultationFrequency: String,
)
