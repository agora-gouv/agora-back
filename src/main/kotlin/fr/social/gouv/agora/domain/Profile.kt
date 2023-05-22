package fr.social.gouv.agora.domain

data class Profile(
    val gender: Gender,
    val yearOfBirth: Int,
    val department: Department,
    val cityType: CityType,
    val jobCategory: JobCategory,
    val voteFrequency: Frequency,
    val publicMeetingFrequency: Frequency,
    val consultationFrequency: Frequency,
)

data class ProfileInserting(
    val gender: Gender,
    val yearOfBirth: Int,
    val department: Department,
    val cityType: CityType,
    val jobCategory: JobCategory,
    val voteFrequency: Frequency,
    val publicMeetingFrequency: Frequency,
    val consultationFrequency: Frequency,
    val userId: String,
)

enum class Gender {
    MASCULIN, FEMININ_F, AUTRE_A
}

enum class CityType {
    RURAL_R, URBAIN_U, AUTRE_A,
}

enum class JobCategory {
    AGRICULTEUR_AG, ARTISAN_AR, CADRE_CA, PROFESSION_INTERMEDIAIRE_PI, EMPLOYE_EM, OUVRIER_OU, NON_DETERMINE_ND, UNKNOWN_UN,
}

enum class Frequency {
    SOUVENT_S, PARFOIS_P, JAMAIS_J,
}
