package fr.social.gouv.agora.domain

data class Profile(
    val gender: Gender?,
    val yearOfBirth: Int?,
    val department: Department?,
    val cityType: CityType?,
    val jobCategory: JobCategory?,
    val voteFrequency: Frequency?,
    val publicMeetingFrequency: Frequency?,
    val consultationFrequency: Frequency?,
)

data class ProfileInserting(
    val gender: Gender?,
    val yearOfBirth: Int?,
    val department: Department?,
    val cityType: CityType?,
    val jobCategory: JobCategory?,
    val voteFrequency: Frequency?,
    val publicMeetingFrequency: Frequency?,
    val consultationFrequency: Frequency?,
    val userId: String,
)

enum class Gender {
    MASCULIN, FEMININ, AUTRE
}

enum class CityType {
    RURAL, URBAIN, AUTRE,
}

enum class JobCategory {
    AGRICULTEUR, ARTISAN, CADRE, PROFESSION_INTERMEDIAIRE, EMPLOYE, OUVRIER, ETUDIANTS, RETRAITES, AUTRESOUSANSACTIVITEPRO, UNKNOWN,
}

enum class Frequency {
    SOUVENT, PARFOIS, JAMAIS,
}
