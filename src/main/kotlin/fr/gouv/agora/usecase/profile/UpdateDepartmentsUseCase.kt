package fr.gouv.agora.usecase.profile

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.domain.exceptions.InvalidNumberOfDepartmentsException
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.springframework.stereotype.Service

@Service
class UpdateDepartmentsUseCase(
    private val profileRepository: ProfileRepository,
    private val authentificationHelper: AuthentificationHelper,
) {
    fun execute(departmentsInput: List<String>) {
        val userId = authentificationHelper.getUserId()!!
        if (departmentsInput.size !in 0..2) throw InvalidNumberOfDepartmentsException()

        val departments = departmentsInput.map { Territoire.Departement.fromOrThrow(it) }

        profileRepository.updateDepartments(userId, departments.getOrNull(0), departments.getOrNull(1))
    }
}
