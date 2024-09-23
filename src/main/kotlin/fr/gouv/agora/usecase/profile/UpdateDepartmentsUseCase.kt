package fr.gouv.agora.usecase.profile

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class UpdateDepartmentsUseCase(
    private val profileRepository: ProfileRepository,
    private val authentificationHelper: AuthentificationHelper,
) {
    fun execute(departmentsInput: List<String>) {
        val userId = authentificationHelper.getUserId()!!
        val departments = departmentsInput.map { Territoire.Departement.from(it) }

        if (departments.size  in 0..2) throw Exception()

        profileRepository.updateDepartments(userId, departments.getOrNull(0), departments.getOrNull(1))
    }
}
