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
        conditionParticipation: List<String>?,
        modaliteParticipation: List<String>?,
        anneeDeLancement: String?
    ): StrapiDTO<FicheInventaireStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("fiche-inventaires")
            .filters {
                addThematique(thematique)
                addEtape(etape)
                addCondition(conditionParticipation)
                addModalite(modaliteParticipation)
                addAnneeDeLancement(anneeDeLancement)
                addTitre(titre)
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
