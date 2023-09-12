package fr.social.gouv.agora.infrastructure.qagPaginated.repository

import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.qagPaginated.repository.QagDateFreezeRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
@Suppress("unused")
class QagDateFreezeRepositoryImpl(
    private val cacheRepository: QagDateFreezeCacheRepository,
) : QagDateFreezeRepository {

    override fun initQagDateFreeze(userId: String, thematiqueId: String?): Date {
        val localDateTime = LocalDateTime.now()
        cacheRepository.putQagDateFreeze(userId = userId, thematiqueId = thematiqueId, date = localDateTime)
        return localDateTime.toDate()
    }

    override fun getQagDateFreeze(userId: String, thematiqueId: String?): Date {
        val dateFreeze = cacheRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        return dateFreeze?.toDate() ?: initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
    }

}