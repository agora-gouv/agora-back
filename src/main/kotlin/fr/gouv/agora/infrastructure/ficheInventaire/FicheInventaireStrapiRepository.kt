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

    fun getFichesInventaire(filters: FicheInventaireFilters): StrapiDTO<FicheInventaireStrapiDTO> {
        val uriBuilder = StrapiRequestBuilder("fiche-inventaires")
            if (filters.titre != null) uriBuilder.contains("titre", filters.titre)
            if (filters.thematique != null) uriBuilder.filterIn(listOf("thematique", "id"), listOf(filters.thematique) )
            if (filters.etape != null) uriBuilder.filterIn("etape", filters.etape)
            if (filters.conditionParticipation != null) uriBuilder.filterIn("condition_participation", filters.conditionParticipation)
            if (filters.modaliteParticipation != null) uriBuilder.filterIn("modalite_participation", filters.modaliteParticipation)
            if (filters.anneeDeLancement != null) uriBuilder.filterIn("annee_de_lancement", listOf(filters.anneeDeLancement))

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
