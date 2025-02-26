package fr.gouv.agora.infrastructure.profile

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.profile.GetProfileUseCase
import fr.gouv.agora.usecase.profile.InsertProfileUseCase
import fr.gouv.agora.usecase.profile.UpdateDepartmentsUseCase
import fr.gouv.agora.usecase.profile.repository.ProfileEditResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Profil")
class ProfileController(
    private val insertProfileUseCase: InsertProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val jsonMapper: ProfileJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
    private val updateDepartementsUseCase: UpdateDepartmentsUseCase,
) {
    @PostMapping("/profile")
    fun postProfile(
        @RequestBody profileJson: ProfileJson,
    ): HttpEntity<*> {
        val profile = jsonMapper.toDomain(
            json = profileJson,
            userId = authentificationHelper.getUserId()!!,
        )
        return when (profile?.let { insertProfileUseCase.insertProfile(profile) } ?: ProfileEditResult.FAILURE) {
            ProfileEditResult.SUCCESS -> ResponseEntity.ok().body("")
            ProfileEditResult.FAILURE -> ResponseEntity.status(400).body("")
        }
    }

    @GetMapping("/profile")
    @Operation(summary = "Récupérer les informations du profil")
    fun getProfile(): HttpEntity<*> {
        val profile = getProfileUseCase.getProfile(authentificationHelper.getUserId()!!)
        return ResponseEntity.ok().body(jsonMapper.toJson(profile))
    }


    @PostMapping("/profile/departments")
    fun updateDepartments(
        @RequestBody departments: ProfileDepartmentJson,
    ): HttpEntity<Nothing> {
        updateDepartementsUseCase.execute(departments)
        return ResponseEntity.ok().build()
    }
}
