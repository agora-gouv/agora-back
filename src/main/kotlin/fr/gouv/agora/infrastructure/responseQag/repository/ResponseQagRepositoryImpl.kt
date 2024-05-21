package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Component

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

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.Strapi)) {
            val strapiResponses = strapiRepository.getResponsesQag(qagUUIDs).let(mapper::toDomain)

            return databaseResponses + strapiResponses
        }

        return databaseResponses
    }

    override fun getResponseQag(qagId: String): ResponseQag? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            databaseRepository.getResponseQag(qagId = qagUUID)?.let(mapper::toDomain)
        }
    }

    override fun getResponsesQagCount(): Int {
        return databaseRepository.getResponsesQagCount()
    }

    override fun getResponsesQag(offset: Int): List<ResponseQag> {
        return databaseRepository.getResponsesQag(offset = offset).mapNotNull(mapper::toDomain)
    }
}
