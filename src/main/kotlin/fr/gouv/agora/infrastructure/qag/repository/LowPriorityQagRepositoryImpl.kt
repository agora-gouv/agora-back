package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.qag.repository.LowPriorityQagRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class LowPriorityQagRepositoryImpl(
    private val databaseRepository: LowPriorityQagDatabaseRepository,
) : LowPriorityQagRepository {

    override fun getLowPriorityQagIds(qagIds: List<String>): List<String> {
        return databaseRepository.getLowPriorityQagIds(qagIds.mapNotNull { it.toUuidOrNull() }).map { it.toString() }
    }

}