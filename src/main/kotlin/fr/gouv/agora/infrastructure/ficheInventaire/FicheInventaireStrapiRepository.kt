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

    fun getFichesInventaire(thematique: String?, etape: String?, modalite_participation: List<String>?): StrapiDTO<FicheInventaireStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("fiche-inventaires")
            if (thematique != null) {
                uriBuilder.filterBy(listOf("thematique", "id_base_de_donnees"), listOf(thematique))
            }
            if (etape != null) {
                uriBuilder.filterBy("etape", listOf(etape))
            }
            if (!modalite_participation.isNullOrEmpty()) {
                uriBuilder.filterBy("modalite_participation", modalite_participation)
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
