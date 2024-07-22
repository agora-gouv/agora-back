package fr.gouv.agora.infrastructure.profile

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.profile.GetProfileUseCase
import fr.gouv.agora.usecase.profile.InsertProfileUseCase
import fr.gouv.agora.usecase.profile.repository.ProfileEditResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
@Tag(name = "Profil")
class ProfileController(
    private val insertProfileUseCase: InsertProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val jsonMapper: ProfileJsonMapper,
) {
    @PostMapping("/profile")
    fun postProfile(
        // TODO : Modifier la gestion des JWT pour pas avoir de paramètre authorizationHeader dans tout les controllers
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
        @RequestBody profileJson: ProfileJson,
    ): HttpEntity<*> {
        val profile = jsonMapper.toDomain(
            json = profileJson,
            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
        )
        return when (profile?.let { insertProfileUseCase.insertProfile(profile) } ?: ProfileEditResult.FAILURE) {
            ProfileEditResult.SUCCESS -> ResponseEntity.ok().body("")
            ProfileEditResult.FAILURE -> ResponseEntity.status(400).body("")
        }
    }

    @GetMapping("/profile")
    @Operation(summary = "Récupérer les informations du profil")
    fun getProfile(
        @RequestHeader("Authorization", required = false) authorizationHeader: String,
    ): HttpEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val profile = getProfileUseCase.getProfile(userId)
        return ResponseEntity.ok().body(jsonMapper.toJson(profile))
    }
}
