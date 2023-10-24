package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.social.gouv.agora.usecase.qag.repository.QagPreviewCacheRepository
import fr.social.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class QagPreviewListUseCaseV2(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val cacheRepository: QagPreviewCacheRepository,
    private val supportQagUseCase: SupportQagUseCase,
    private val mapper: QagPreviewMapper,
) {

    fun getQagPreviewList(userId: String, thematiqueId: String?): QagPreviewList {
        val supportedQagIds = supportQagUseCase.getUserSupportedQagIds(userId)
        return QagPreviewList(
            popularPreviewList = getPopularQags(thematiqueId = thematiqueId)
                .map { qag ->
                    mapper.toPreview(
                        qag = qag,
                        isSupportedByUser = supportedQagIds.any { qagId -> qagId == qag.qagInfo.id },
                        isAuthor = userId == qag.qagInfo.userId,
                    )
                },
            latestPreviewList = getLatestQags(thematiqueId = thematiqueId)
                .map { qag ->
                    mapper.toPreview(
                        qag = qag,
                        isSupportedByUser = supportedQagIds.any { qagId -> qagId == qag.qagInfo.id },
                        isAuthor = userId == qag.qagInfo.userId,
                    )
                },
            supportedPreviewList = getSupportedQags(
                userId = userId,
                thematiqueId = thematiqueId,
            ).map { qag ->
                mapper.toPreview(
                    qag = qag,
                    isSupportedByUser = supportedQagIds.any { qagId -> qagId == qag.qagInfo.id },
                    isAuthor = userId == qag.qagInfo.userId,
                )
            },
        )
    }

    private fun getPopularQags(thematiqueId: String?): List<QagWithSupportCount> {
        return cacheRepository.getQagPopularList(thematiqueId = thematiqueId)
            ?: qagInfoRepository.getPopularQags(thematiqueId = thematiqueId)
                .mapQags()
                .also { cacheRepository.initQagPopularList(thematiqueId = thematiqueId, qagList = it) }
    }

    private fun getLatestQags(thematiqueId: String?): List<QagWithSupportCount> {
        return cacheRepository.getQagLatestList(thematiqueId = thematiqueId)
            ?: qagInfoRepository.getLatestQags(thematiqueId = thematiqueId)
                .mapQags()
                .also { cacheRepository.initQagLatestList(thematiqueId = thematiqueId, qagList = it) }

    }

    private fun getSupportedQags(userId: String, thematiqueId: String?): List<QagWithSupportCount> {
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

data class QagPreviewList(
    val popularPreviewList: List<QagPreview>,
    val latestPreviewList: List<QagPreview>,
    val supportedPreviewList: List<QagPreview>,
)