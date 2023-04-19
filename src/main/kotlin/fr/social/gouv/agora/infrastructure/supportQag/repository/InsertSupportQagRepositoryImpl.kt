package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.supportQag.repository.InsertSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.InsertSupportQagResult
import org.springframework.stereotype.Component

@Component
class InsertSupportQagRepositoryImpl(
    private val databaseRepository: InsertSupportQagDatabaseRepository,
    private val mapper: SupportQagMapper,
) : InsertSupportQagRepository {

    override fun insertSupportQag(supportQag: SupportQagInserting): InsertSupportQagResult {
            return mapper.toDto(supportQag)?.let { supportQagDto ->
                InsertSupportQagResult.SUCCESS
            } ?: InsertSupportQagResult.FAILURE
    }

}