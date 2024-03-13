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

    override fun getLatestConsultationUpdateLabel(consultationId: String): String? {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            updateDatabaseRepository.getLatestConsultationUpdateLabel(consultationUUID)
        }
    }

    override fun getUnansweredUsersConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            val updateDTO = updateDatabaseRepository.getUnansweredUsersConsultationUpdate(consultationUUID)
            val sectionDTOs = sectionDatabaseRepository.getConsultationUpdateSections(updateDTO.id)
            mapper.toDomain(updateDTO, sectionDTOs)
        }
    }

    override fun getLatestConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            val updateDTO = updateDatabaseRepository.getLatestConsultationUpdate(consultationUUID)
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
                updateDatabaseRepository.getConsultationUpdate(
                    consultationId = consultationUUID,
                    consultationUpdateId = consultationUpdateUUID,
                )?.let { updateDTO ->
                    mapper.toDomain(
                        updateDTO,
                        sectionDatabaseRepository.getConsultationUpdateSections(consultationUpdateUUID),
                    )
                }
            }
        }
    }

}