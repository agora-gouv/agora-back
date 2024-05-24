package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
@Suppress("unused")
class ResponseQagRepositoryImpl(
    private val databaseRepository: ResponseQagDatabaseRepository,
    private val strapiRepository: ResponseQagStrapiRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val mapper: ResponseQagMapper,
) : ResponseQagRepository {
    override fun getResponsesQag(qagIds: List<String>): List<ResponseQag> {
        val qagUUIDs = qagIds.mapNotNull { it.toUuidOrNull() }

        val databaseResponses = databaseRepository.getResponsesQag(qagUUIDs).mapNotNull(mapper::toDomain)

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.Strapi)) return databaseResponses

        val strapiResponses = strapiRepository.getResponsesQag(qagUUIDs).let(mapper::toDomain)

        return databaseResponses + strapiResponses
    }

    override fun getResponseQag(qagId: String): ResponseQag? {
        val qagUUID = qagId.toUuidOrNull() ?: return null

        val databaseResponse = databaseRepository.getResponseQag(qagId = qagUUID)?.let(mapper::toDomain)

        if (databaseResponse != null || !featureFlagsRepository.isFeatureEnabled(AgoraFeature.Strapi)) return databaseResponse

        return strapiRepository.getResponsesQag(listOf(qagUUID)).let(mapper::toDomain).firstOrNull()
    }

    override fun getResponsesQagCount(): Int {
        val responsesQagCountOnDatabase = databaseRepository.getResponsesQagCount()

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.Strapi)) return responsesQagCountOnDatabase

        return strapiRepository.getResponsesCount() + responsesQagCountOnDatabase
    }

    override fun getResponsesQag(offset: Int): List<ResponseQag> {
        val databaseResponses = databaseRepository.getResponsesQag().mapNotNull(mapper::toDomain)

        val databaseToIndex = min(databaseResponses.size - offset, offset + 20)

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.Strapi)) return databaseResponses.subList(offset, databaseToIndex)

        val strapiResponses = strapiRepository.getResponsesQag().let(mapper::toDomain)

        val responsesQag = databaseResponses + strapiResponses

        val toIndex = min(responsesQag.size - offset, offset + 20)

        return responsesQag
            .sortedByDescending { it.responseDate }
            .subList(offset, toIndex)
    }
}
