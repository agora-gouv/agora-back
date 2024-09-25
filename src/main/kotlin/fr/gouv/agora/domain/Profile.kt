package fr.gouv.agora.domain

data class Profile(
    val gender: Gender?,
    val yearOfBirth: Int?,
    val department: Department?,
    val cityType: CityType?,
    val jobCategory: JobCategory?,
    val voteFrequency: Frequency?,
    val publicMeetingFrequency: Frequency?,
    val consultationFrequency: Frequency?,
    val primaryDepartment: Territoire.Departement?,
    val secondaryDepartment: Territoire.Departement?,
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

enum class AgeRange {
    LESS_THAN_18,
    BETWEEN_18_AND_25,
    BETWEEN_26_AND_35,
    BETWEEN_36_AND_45,
    BETWEEN_46_AND_55,
    BETWEEN_56_AND_65,
    MORE_THAN_65
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
