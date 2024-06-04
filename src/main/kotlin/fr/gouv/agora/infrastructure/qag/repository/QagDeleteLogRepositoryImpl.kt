package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.domain.QagDeleteLog
import fr.gouv.agora.usecase.qag.repository.QagDeleteLogRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagDeleteLogRepositoryImpl(
    private val databaseRepository: QagDeleteLogDatabaseRepository,
    private val mapper: QagDeleteLogMapper,
) : QagDeleteLogRepository {

    override fun insertQagDeleteLog(qagDeleteLog: QagDeleteLog) {
        mapper.toDto(qagDeleteLog)?.let { qagDeleteLogDTO -> databaseRepository.save(qagDeleteLogDTO) }
    }
}
