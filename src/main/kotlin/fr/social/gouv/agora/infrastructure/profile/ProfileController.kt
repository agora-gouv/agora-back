package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.usecase.login.LoginUseCase
import fr.social.gouv.agora.usecase.profile.GetProfileUseCase
import fr.social.gouv.agora.usecase.profile.InsertProfileUseCase
import fr.social.gouv.agora.usecase.profile.repository.ProfileInsertionResult
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")

class ProfileController(
    private val insertProfileUseCase: InsertProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val loginUseCase: LoginUseCase,
    private val jsonMapper: ProfileJsonMapper,
) {
    @PostMapping("/profile")
    fun postProfile(
        @RequestHeader deviceId: String,
        @RequestBody profileJson: ProfileJson,
    ): HttpEntity<*> {
        val userId = loginUseCase.getUser(deviceId)?.userId
        val profile = userId?.let { jsonMapper.toDomain(profileJson, userId) }
        return when (profile?.let { insertProfileUseCase.insertProfile(profile) } ?: ProfileInsertionResult.FAILURE) {
            ProfileInsertionResult.SUCCESS -> ResponseEntity.status(200).body("")
            ProfileInsertionResult.FAILURE -> ResponseEntity.status(400).body("")
        }
    }

    @GetMapping("/profile")
    fun getProfile(
        @RequestHeader deviceId: String,
    ): HttpEntity<*> {
        val userId = loginUseCase.getUser(deviceId)?.userId
        return when (val profile = userId?.let { getProfileUseCase.getProfile(userId) }) {
            null -> ResponseEntity.status(400).body("")
            else -> ResponseEntity.status(200).body(jsonMapper.toJson(profile))
        }
    }
}