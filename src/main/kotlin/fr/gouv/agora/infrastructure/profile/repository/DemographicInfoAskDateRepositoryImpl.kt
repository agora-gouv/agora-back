package fr.gouv.agora.infrastructure.profile.repository

import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class DemographicInfoAskDateRepositoryImpl(
    private val cacheRepository: DemographicInfoAskDateCacheRepository,
    private val databaseRepository: DemographicInfoAskDateDatabaseRepository,
    private val mapper: DemographicInfoAskDateMapper,
) : DemographicInfoAskDateRepository {

    override fun getDate(userId: String): LocalDate? {
        return userId.toUuidOrNull()?.let { userUUID ->
            cacheRepository.getDate(userUUID) ?: getDateFromDatabase(userUUID)
        }
    }

    override fun insertDate(userId: String) {
        userId.toUuidOrNull()
            ?.let { userUUID -> mapper.toDto(userUUID) }
            ?.let { demographicInfoAskDateDTO ->
                val savedDemographicInfoAskDateDTO = databaseRepository.save(demographicInfoAskDateDTO)
                cacheRepository.insertDate(
                    savedDemographicInfoAskDateDTO.userId,
                    mapper.toDate(savedDemographicInfoAskDateDTO),
                )
            }
    }

    override fun deleteDate(userId: String) {
        userId.toUuidOrNull()?.let { userUUID ->
            cacheRepository.deleteDate(userUUID)
            databaseRepository.deleteAskDate(userUUID)
        }
    }

    private fun getDateFromDatabase(userUUID: UUID): LocalDate? {
        return databaseRepository.getAskDate(userUUID)?.let { demographicInfoAskDateDTO ->
            mapper.toDate(demographicInfoAskDateDTO)
        }?.also { askDate -> cacheRepository.insertDate(userUUID, askDate) }
    }

}
