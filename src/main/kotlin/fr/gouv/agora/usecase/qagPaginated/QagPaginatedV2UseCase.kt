package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.domain.Header
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.infrastructure.qag.repository.QagListWithMaxPageCount
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderCacheRepository
import fr.gouv.agora.usecase.qagPaginated.repository.HeaderRepository
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class QagPaginatedV2UseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
    private val headerRepository: HeaderRepository,
    private val headerCacheRepository: HeaderCacheRepository,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
        private const val TOP = "top"
        private const val LATEST = "latest"
        private const val SUPPORTING = "supporting"
    }

    fun getPopularQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount =
            qagListsCacheRepository.getQagPopularList(thematiqueId = thematiqueId, pageNumber = pageNumber)
                ?: getQagPaginated(
                    getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getPopularQagsPaginatedV2),
                    userId = userId,
                    pageNumber = pageNumber,
                    thematiqueId = thematiqueId
                )?.also {
                    qagListsCacheRepository.initQagPopularList(
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        qagListWithMaxPageCount = it
                    )
                }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = TOP, pageNumber = pageNumber)
    }

    fun getLatestQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount =
            qagListsCacheRepository.getQagLatestList(thematiqueId = thematiqueId, pageNumber = pageNumber)
                ?: getQagPaginated(
                    getQagMethod = RetrieveQagMethod.WithoutUserId(QagInfoRepository::getLatestQagsPaginatedV2),
                    userId = userId,
                    pageNumber = pageNumber,
                    thematiqueId = thematiqueId
                )?.also {
                    qagListsCacheRepository.initQagLatestList(
                        thematiqueId = thematiqueId,
                        pageNumber = pageNumber,
                        qagListWithMaxPageCount = it
                    )
                }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = LATEST, pageNumber = pageNumber)
    }


    fun getSupportedQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCountV2? {
        val qagListWithMaxPageCount = qagListsCacheRepository.getQagSupportedList(
            userId = userId,
            thematiqueId = thematiqueId,
            pageNumber = pageNumber
        )
            ?: getQagPaginated(
                getQagMethod = RetrieveQagMethod.WithUserId(QagInfoRepository::getSupportedQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId
            )?.also {
                qagListsCacheRepository.initQagSupportedList(
                    userId = userId,
                    thematiqueId = thematiqueId,
                    pageNumber = pageNumber,
                    qagListWithMaxPageCount = it
                )
            }
        return qagListWithMaxPageCount?.mapQags(userId = userId, filterType = SUPPORTING, pageNumber = pageNumber)
    }

    private fun getQagPaginated(
        getQagMethod: RetrieveQagMethod,
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagListWithMaxPageCount? {
        if (pageNumber < 1) return null
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)
        val qagsCount = when (getQagMethod) {
            is RetrieveQagMethod.WithUserId -> userSupportedQagIds.size
            is RetrieveQagMethod.WithoutUserId -> qagInfoRepository.getQagsCount()
        }
        if (offset > qagsCount) return null
        val thematiques = thematiqueRepository.getThematiqueList()

        val qags = when (getQagMethod) {
            is RetrieveQagMethod.WithUserId -> {
                getQagMethod.method.invoke(
                    qagInfoRepository,
                    userId,
                    offset,
                    thematiqueId,
                )
            }

            is RetrieveQagMethod.WithoutUserId -> {
                getQagMethod.method.invoke(
                    qagInfoRepository,
                    offset,
                    thematiqueId,
                )
            }
        }
        val qagWithSupportCountList = qags.mapNotNull { qagInfoWithSupportCount ->
            thematiques.find { thematique -> thematique.id == qagInfoWithSupportCount.thematiqueId }
                ?.let { QagWithSupportCount(qagInfo = qagInfoWithSupportCount, thematique = it) }
        }
        val maxPageCount = ceil(qagsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt()
        return QagListWithMaxPageCount(maxPageCount = maxPageCount, qags = qagWithSupportCountList)
    }

    private fun QagListWithMaxPageCount.mapQags(
        userId: String,
        filterType: String,
        pageNumber: Int,
    ): QagsAndMaxPageCountV2 {
        val header = when (pageNumber) {
            1 -> headerCacheRepository.getHeader(filterType) ?: headerRepository.getHeader(filterType)
                ?.also { headerCacheRepository.initHeader(filterType, it) }

            else -> null
        }
        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)
        val qagList = this.qags.map { qagWithSupportCount ->
            mapper.toPreview(
                qag = qagWithSupportCount,
                isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                isAuthor = qagWithSupportCount.qagInfo.userId == userId
            )
        }
        return QagsAndMaxPageCountV2(
            maxPageCount = this.maxPageCount,
            header = header,
            qags = qagList
        )
    }
}

data class QagsAndMaxPageCountV2(
    val qags: List<QagPreview>,
    val header: Header?,
    val maxPageCount: Int,
)

private sealed class RetrieveQagMethod {
    data class WithoutUserId(
        val method: QagInfoRepository.(offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()

    data class WithUserId(
        val method: QagInfoRepository.(userId: String, offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : RetrieveQagMethod()
}

