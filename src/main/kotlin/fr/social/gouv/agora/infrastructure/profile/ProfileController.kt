package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
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
    private val jsonMapper: ProfileJsonMapper,
) {
    @PostMapping("/profile")
    fun postProfile(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody profileJson: ProfileJson,
    ): HttpEntity<*> {
        val profile = jsonMapper.toDomain(
            json = profileJson,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return when (profile?.let { insertProfileUseCase.insertProfile(profile) } ?: ProfileInsertionResult.FAILURE) {
            ProfileInsertionResult.SUCCESS -> ResponseEntity.ok().body("")
            ProfileInsertionResult.FAILURE -> ResponseEntity.status(400).body("")
        }
    }

    @GetMapping("/profile")
    fun getProfile(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val profile = getProfileUseCase.getProfile(userId)
        return ResponseEntity.ok().body(jsonMapper.toJson(profile))
    }
}