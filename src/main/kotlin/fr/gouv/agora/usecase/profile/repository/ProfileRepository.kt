package fr.gouv.agora.usecase.profile.repository

import fr.gouv.agora.domain.Profile
import fr.gouv.agora.domain.ProfileInserting
import fr.gouv.agora.domain.Territoire.Departement

interface ProfileRepository {
    fun getProfile(userId: String): Profile?
    fun updateProfile(profileInserting: ProfileInserting): ProfileEditResult
    fun insertProfile(profileInserting: ProfileInserting): ProfileEditResult
    fun deleteUsersProfile(userIDs: List<String>)
    fun updateDepartments(userId: String, primaryDepartment: Departement?, secondaryDepartment: Departement?)
}

enum class ProfileEditResult {
    SUCCESS, FAILURE
}
