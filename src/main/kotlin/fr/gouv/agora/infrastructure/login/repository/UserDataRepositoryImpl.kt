package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class UserDataRepositoryImpl(
    private val databaseRepository: UserDataDatabaseRepository,
    private val mapper: UserDataMapper,
) : UserDataRepository {

    override fun addUserData(loginRequest: LoginRequest) {
        databaseRepository.save(mapper.toDto(loginRequest = loginRequest))
    }

    override fun addUserData(signupRequest: SignupRequest, generatedUserId: String) {
        databaseRepository.save(mapper.toDto(signupRequest = signupRequest, userId = generatedUserId))
    }

}