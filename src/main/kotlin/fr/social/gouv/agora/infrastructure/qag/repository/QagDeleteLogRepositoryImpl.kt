package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagDeleteLog
import fr.social.gouv.agora.usecase.qag.repository.QagDeleteLogRepository
import org.springframework.stereotype.Component
import java.util.*

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
