package fr.gouv.agora.infrastructure.ficheInventaire

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import org.springframework.stereotype.Repository

@Repository
class FicheInventaireStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    val ref = object : TypeReference<StrapiDTO<FicheInventaireStrapiDTO>>() {}

    fun getFichesInventaire(
        titre: String?,
        thematique: String?,
        etape: List<String>?,
        modaliteParticipation: List<String>?,
        anneeDeLancement: String?
    ): StrapiDTO<FicheInventaireStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("fiche-inventaires")
        if (thematique != null) {
            uriBuilder.filterBy(listOf("thematique", "id"), listOf(thematique))
        }
        if (!etape.isNullOrEmpty()) {
            uriBuilder.filterBy("etape", etape)
        }
        if (!modaliteParticipation.isNullOrEmpty()) {
            uriBuilder.filterBy("modalite_participation", modaliteParticipation)
        }
        if (anneeDeLancement != null) {
            uriBuilder.filterBy("annee_de_lancement", listOf(anneeDeLancement))
        }
        if (titre != null) {
            uriBuilder.filterIn("titre", listOf(titre))
        }
        return cmsStrapiHttpClient.request(uriBuilder, ref)
    }

    fun getFicheInventaire(ficheId: String): StrapiAttributes<FicheInventaireStrapiDTO>? {
        val strapiFicheId = ficheId.toIntOrNull() ?: return null
        val uriBuilder = StrapiRequestBuilder("fiche-inventaires")
            .getByIds(listOf(strapiFicheId))

        return cmsStrapiHttpClient.request<FicheInventaireStrapiDTO>(uriBuilder, ref).data
            .firstOrNull()
    }
}
