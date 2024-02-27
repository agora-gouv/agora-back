package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.*
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateSectionDTO
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateV2DTO
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateInfoV2Mapper {

    companion object {
        private const val TRUE_INT_VALUE = 1

        private const val SECTION_TYPE_TITLE = "title"
        private const val SECTION_TYPE_RICH_TEXT = "richText"
        private const val SECTION_TYPE_IMAGE = "image"
        private const val SECTION_TYPE_VIDEO = "video"
        private const val SECTION_TYPE_FOCUS_NUMBER = "focusNumber"
        private const val SECTION_TYPE_QUOTE = "quote"
    }

    fun toDomain(
        dto: ConsultationUpdateV2DTO,
        sectionDTOs: List<ConsultationUpdateSectionDTO>,
    ): ConsultationUpdateInfoV2 {
        return ConsultationUpdateInfoV2(
            id = dto.id.toString(),
            updateDate = dto.updateDate,
            shareTextTemplate = dto.shareTextTemplate,
            hasQuestionsInfo = dto.hasQuestionsInfo == TRUE_INT_VALUE,
            hasParticipationInfo = dto.hasParticipationInfo == TRUE_INT_VALUE,
            responsesInfo = buildResponsesInfo(dto),
            infoHeader = buildInfoHeader(dto),
            body = buildBody(
                sectionDTOs = sectionDTOs,
                filter = { sectionDTO -> sectionDTO.isPreview != TRUE_INT_VALUE },
            ),
            bodyPreview = buildBody(
                sectionDTOs = sectionDTOs,
                filter = { sectionDTO -> sectionDTO.isPreview == TRUE_INT_VALUE },
            ),
            downloadAnalysisUrl = dto.downloadAnalysisUrl,
            footer = buildFooter(dto),
        )
    }

    private fun buildResponsesInfo(dto: ConsultationUpdateV2DTO): ResponsesInfo? {
        if (dto.responsesInfoPicto == null || dto.responsesInfoDescription == null) return null
        return ResponsesInfo(
            picto = dto.responsesInfoPicto,
            description = dto.responsesInfoDescription,
        )
    }

    private fun buildInfoHeader(dto: ConsultationUpdateV2DTO): InfoHeader? {
        if (dto.infoHeaderPicto == null || dto.infoHeaderDescription == null) return null
        return InfoHeader(
            picto = dto.infoHeaderPicto,
            description = dto.infoHeaderDescription,
        )
    }

    private fun buildFooter(dto: ConsultationUpdateV2DTO): Footer? {
        return dto.footerDescription?.let {
            Footer(
                title = dto.footerTitle,
                description = dto.footerDescription,
            )
        }
    }

    private fun buildBody(
        sectionDTOs: List<ConsultationUpdateSectionDTO>,
        filter: (ConsultationUpdateSectionDTO) -> Boolean = { _ -> true },
    ): List<Section> {
        return sectionDTOs.filter(filter).mapNotNull { sectionDTO ->
            when (sectionDTO.type) {
                SECTION_TYPE_TITLE -> buildTitleSection(sectionDTO)
                SECTION_TYPE_RICH_TEXT -> buildRichTextSection(sectionDTO)
                SECTION_TYPE_IMAGE -> buildImageSection(sectionDTO)
                SECTION_TYPE_VIDEO -> buildVideoSection(sectionDTO)
                SECTION_TYPE_FOCUS_NUMBER -> buildFocusNumberSection(sectionDTO)
                SECTION_TYPE_QUOTE -> buildQuoteSection(sectionDTO)
                else -> null
            }
        }
    }

    private fun buildTitleSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Title? {
        return sectionDTO.title?.let(Section::Title)
    }

    private fun buildRichTextSection(sectionDTO: ConsultationUpdateSectionDTO): Section.RichText? {
        return sectionDTO.description?.let(Section::RichText)
    }

    private fun buildImageSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Image? {
        if (sectionDTO.url == null || sectionDTO.description == null) return null
        return Section.Image(
            url = sectionDTO.url,
            contentDescription = sectionDTO.description,
        )
    }

    private fun buildVideoSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Video? {
        if (
            sectionDTO.url == null
            || sectionDTO.videoWidth == null
            || sectionDTO.videoHeight == null
            || sectionDTO.videoTranscription == null
        ) return null
        return Section.Video(
            url = sectionDTO.url,
            width = sectionDTO.videoWidth,
            height = sectionDTO.videoHeight,
            authorInfo = buildAuthorInfo(sectionDTO),
            transcription = sectionDTO.videoTranscription,
        )
    }

    private fun buildAuthorInfo(sectionDTO: ConsultationUpdateSectionDTO): Section.Video.AuthorInfo? {
        if (sectionDTO.authorInfoName == null || sectionDTO.authorInfoMessage == null) return null
        return Section.Video.AuthorInfo(
            name = sectionDTO.authorInfoName,
            message = sectionDTO.authorInfoMessage,
        )
    }

    private fun buildFocusNumberSection(sectionDTO: ConsultationUpdateSectionDTO): Section.FocusNumber? {
        if (sectionDTO.title == null || sectionDTO.description == null) return null
        return Section.FocusNumber(
            title = sectionDTO.title,
            description = sectionDTO.description,
        )
    }

    private fun buildQuoteSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Quote? {
        return sectionDTO.description?.let(Section::Quote)
    }

}