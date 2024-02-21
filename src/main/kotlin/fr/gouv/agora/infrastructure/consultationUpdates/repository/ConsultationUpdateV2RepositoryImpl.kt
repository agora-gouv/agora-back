package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationUpdateV2RepositoryImpl(
    private val updateDatabaseRepository: ConsultationUpdateInfoV2DatabaseRepository,
    private val sectionDatabaseRepository: ConsultationUpdateSectionDatabaseRepository,
    private val mapper: ConsultationUpdateInfoV2Mapper,
) : ConsultationUpdateV2Repository {

    override fun getLastConsultationUpdateLabel(consultationId: String): String? {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            updateDatabaseRepository.getLastConsultationUpdateLabel(consultationUUID)
        }
    }

    override fun getLastConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            val updateDTO = updateDatabaseRepository.getLastConsultationUpdate(consultationUUID)
            val sectionDTOs = sectionDatabaseRepository.getConsultationUpdateSections(updateDTO.id)
            mapper.toDomain(updateDTO, sectionDTOs)
        }
    }

    override fun getConsultationUpdate(
        consultationId: String,
        consultationUpdateId: String,
    ): ConsultationUpdateInfoV2? {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            consultationUpdateId.toUuidOrNull()?.let { consultationUpdateUUID ->
                updateDatabaseRepository.getConsultationUpdate(consultationUpdateUUID)?.let { updateDTO ->
                    mapper.toDomain(
                        updateDTO,
                        sectionDatabaseRepository.getConsultationUpdateSections(consultationUUID)
                    )
                }
            }
        }
    }

}