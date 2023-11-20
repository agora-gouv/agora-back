package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
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
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getPopularQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        return qagListsCacheRepository.getQagPopularList(thematiqueId = thematiqueId, pageNumber = pageNumber)
            ?: getQagPaginated(
                getQagMethod = GetQagMethodNew.WithoutUserId(QagInfoRepository::getPopularQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId,
            )?.also {
                qagListsCacheRepository.initQagPopularList(
                    thematiqueId = thematiqueId,
                    pageNumber = pageNumber,
                    qagsAndMaxPageCount = it
                )
            }
    }

    fun getLatestQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        return qagListsCacheRepository.getQagLatestList(thematiqueId = thematiqueId, pageNumber = pageNumber)
            ?: getQagPaginated(
                getQagMethod = GetQagMethodNew.WithoutUserId(QagInfoRepository::getLatestQagsPaginatedV2),
                userId = userId,
                pageNumber = pageNumber,
                thematiqueId = thematiqueId,
            )?.also {
                qagListsCacheRepository.initQagLatestList(
                    thematiqueId = thematiqueId,
                    pageNumber = pageNumber,
                    qagsAndMaxPageCount = it
                )
            }
    }

    fun getSupportedQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        return qagListsCacheRepository.getQagSupportedList(
            userId = userId,
            thematiqueId = thematiqueId,
            pageNumber = pageNumber
        ) ?: getQagPaginated(
            getQagMethod = GetQagMethodNew.WithUserId(QagInfoRepository::getSupportedQagsPaginatedV2),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId,
        )?.also {
            qagListsCacheRepository.initQagSupportedList(
                userId = userId,
                thematiqueId = thematiqueId,
                pageNumber = pageNumber,
                qagsAndMaxPageCount = it
            )
        }
    }

    private fun getQagPaginated(
        getQagMethod: GetQagMethodNew,
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        if (pageNumber < 1) return null

        val qagsCount = qagInfoRepository.getQagsCount()
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > qagsCount) return null

        val qags = when (getQagMethod) {
            is GetQagMethodNew.WithUserId -> getQagMethod.method.invoke(
                qagInfoRepository,
                userId,
                offset,
                thematiqueId,
            )

            is GetQagMethodNew.WithoutUserId -> getQagMethod.method.invoke(
                qagInfoRepository,
                offset,
                thematiqueId,
            )
        }
        val thematiques = thematiqueRepository.getThematiqueList()
        val userSupportedQagIds = supportQagRepository.getUserSupportedQags(userId = userId)

        return QagsAndMaxPageCount(
            qags = qags.mapNotNull { qag ->
                thematiques.find { thematique -> thematique.id == qag.thematiqueId }?.let { thematique ->
                    mapper.toPreview(
                        qag = qag,
                        thematique = thematique,
                        isSupportedByUser = userSupportedQagIds.any { qagId -> qagId == qag.id },
                        isAuthor = qag.userId == userId,
                    )
                }
            },
            maxPageCount = ceil(qagsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        )
    }
}

private sealed class GetQagMethodNew {
    data class WithoutUserId(
        val method: QagInfoRepository.(offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : GetQagMethodNew()

    data class WithUserId(
        val method: QagInfoRepository.(userId: String, offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>,
    ) : GetQagMethodNew()
}