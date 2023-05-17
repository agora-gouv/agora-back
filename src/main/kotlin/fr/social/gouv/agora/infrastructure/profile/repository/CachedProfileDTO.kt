package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO

data class CachedProfileDTO(
    val dateDemande: String = "",
    val profileDTO: ProfileDTO? = null) {
    override fun toString(): String {
        return this::class.simpleName+"(dateDemande = $dateDemande, "+ ProfileDTO::class.simpleName + profileDTO.toString() + ")"
    }
}