package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.usecase.profile.InsertProfileUseCase
import fr.social.gouv.agora.usecase.profile.repository.ProfileInsertionResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")

class ProfileController(
    private val insertProfileUseCase: InsertProfileUseCase,
    private val jsonMapper: ProfileJsonMapper,
) {
    @PostMapping("/profile")
    fun postProfile(
        @RequestHeader deviceId: String,
        @RequestBody profileJson: ProfileJson,
    ): HttpEntity<*> {
        val statusInsertion = insertProfileUseCase.insertProfile(
            deviceId = deviceId,
            profile = jsonMapper.toDomain(profileJson)
        )
        return when (statusInsertion) {
            ProfileInsertionResult.SUCCESS -> ResponseEntity.status(200).body("")
            ProfileInsertionResult.FAILURE -> ResponseEntity.status(400).body("")
        }
    }
}