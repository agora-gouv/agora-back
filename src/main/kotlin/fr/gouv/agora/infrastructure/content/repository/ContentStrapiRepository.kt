package fr.gouv.agora.infrastructure.content.repository

import com.fasterxml.jackson.core.type.TypeReference
import fr.gouv.agora.config.CmsStrapiHttpClient
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.common.StrapiSingleTypeDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PagePoserMaQuestionStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageQuestionsAuGouvernementStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageReponseAuxQuestionsAuGouvernementStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrineAccueilStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrineConditionGeneralesStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrineConsultationStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrineDeclarationAccessibiliteStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrineMentionsLegalesStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrinePolitiqueConfidentialiteStrapiDTO
import fr.gouv.agora.infrastructure.content.repository.dto.PageSiteVitrineQuestionAuGouvernementStrapiDTO
import org.springframework.stereotype.Repository

@Repository
class ContentStrapiRepository(
    private val cmsStrapiHttpClient: CmsStrapiHttpClient,
) {
    fun getPageReponseAuxQaG(): StrapiAttributes<PageReponseAuxQuestionsAuGouvernementStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageReponseAuxQuestionsAuGouvernementStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("page-reponse-aux-questions-au-gouvernement")

        return cmsStrapiHttpClient.requestSingleType<PageReponseAuxQuestionsAuGouvernementStrapiDTO>(
            uriBuilder,
            ref
        ).data
    }

    fun getPagePoserMaQuestion(): StrapiAttributes<PagePoserMaQuestionStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PagePoserMaQuestionStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("page-poser-ma-question")

        return cmsStrapiHttpClient.requestSingleType<PagePoserMaQuestionStrapiDTO>(uriBuilder, ref).data
    }

    fun getPageQuestionsAuGouvernement(): StrapiAttributes<PageQuestionsAuGouvernementStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageQuestionsAuGouvernementStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("page-questions-au-gouvernement")

        return cmsStrapiHttpClient.requestSingleType<PageQuestionsAuGouvernementStrapiDTO>(uriBuilder, ref).data
    }

    fun getPageSiteVitrineAccueil(): StrapiAttributes<PageSiteVitrineAccueilStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrineAccueilStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-accueil")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrineAccueilStrapiDTO>(uriBuilder, ref).data
    }

    fun getPageSiteVitrineConditionGenerales(): StrapiAttributes<PageSiteVitrineConditionGeneralesStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrineConditionGeneralesStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-conditions-generales-d-utilisation")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrineConditionGeneralesStrapiDTO>(uriBuilder, ref).data
    }

    fun getPageSiteVitrineConsultation(): StrapiAttributes<PageSiteVitrineConsultationStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrineConsultationStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-consultation")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrineConsultationStrapiDTO>(uriBuilder, ref).data
    }

    fun getPageSiteVitrineDeclarationAccessibilite(): StrapiAttributes<PageSiteVitrineDeclarationAccessibiliteStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrineDeclarationAccessibiliteStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-declaration-d-accessibilite")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrineDeclarationAccessibiliteStrapiDTO>(
            uriBuilder,
            ref
        ).data
    }

    fun getPageSiteVitrineMentionsLegales(): StrapiAttributes<PageSiteVitrineMentionsLegalesStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrineMentionsLegalesStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-mentions-legale")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrineMentionsLegalesStrapiDTO>(uriBuilder, ref).data
    }

    fun getPageSiteVitrinePolitiqueConfidentialite(): StrapiAttributes<PageSiteVitrinePolitiqueConfidentialiteStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrinePolitiqueConfidentialiteStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-politique-de-confidentialite")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrinePolitiqueConfidentialiteStrapiDTO>(
            uriBuilder,
            ref
        ).data
    }

    fun getPageSiteVitrineQuestionAuGouvernement(): StrapiAttributes<PageSiteVitrineQuestionAuGouvernementStrapiDTO> {
        val ref = object : TypeReference<StrapiSingleTypeDTO<PageSiteVitrineQuestionAuGouvernementStrapiDTO>>() {}
        val uriBuilder = StrapiRequestBuilder("site-vitrine-question-au-gouvernement")

        return cmsStrapiHttpClient.requestSingleType<PageSiteVitrineQuestionAuGouvernementStrapiDTO>(
            uriBuilder,
            ref
        ).data
    }
}
