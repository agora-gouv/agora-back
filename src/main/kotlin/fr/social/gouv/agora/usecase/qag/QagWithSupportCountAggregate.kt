package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.social.gouv.agora.usecase.qag.repository.QagWithSupportCountCacheRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class QagWithSupportCountAggregate(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val cacheRepository: QagWithSupportCountCacheRepository,
) {

    fun getPopularQags(thematiqueId: String?): List<QagWithSupportCount> {
        return cacheRepository.getQagPopularList(thematiqueId = thematiqueId)
            ?: qagInfoRepository.getPopularQags(thematiqueId = thematiqueId)
                .mapQags()
                .also { cacheRepository.initQagPopularList(thematiqueId = thematiqueId, qagList = it) }
    }

    fun getLatestQags(thematiqueId: String?): List<QagWithSupportCount> {
        return cacheRepository.getQagLatestList(thematiqueId = thematiqueId)
            ?: qagInfoRepository.getLatestQags(thematiqueId = thematiqueId)
                .mapQags()
                .also { cacheRepository.initQagLatestList(thematiqueId = thematiqueId, qagList = it) }

    }

    fun getSupportedQags(userId: String, thematiqueId: String?): List<QagWithSupportCount> {
        return cacheRepository.getQagSupportedList(userId = userId, thematiqueId = thematiqueId)
            ?: qagInfoRepository.getSupportedQags(userId = userId, thematiqueId = thematiqueId)
                .mapQags()
                .also {
                    cacheRepository.initQagSupportedList(
                        userId = userId,
                        thematiqueId = thematiqueId,
                        qagList = it,
                    )
                }
    }

    private fun List<QagInfoWithSupportCount>.mapQags(): List<QagWithSupportCount> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return this.mapNotNull { qagInfo ->
            thematiqueList.find { thematique -> thematique.id == qagInfo.thematiqueId }?.let { thematique ->
                QagWithSupportCount(
                    qagInfo = qagInfo,
                    thematique = thematique,
                )
            }
        }
    }

}

data class QagWithSupportCount(
    val qagInfo: QagInfoWithSupportCount,
    val thematique: Thematique,
)