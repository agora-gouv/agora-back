package fr.gouv.agora.usecase.qagPaginated

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.usecase.qag.QagPreviewMapper
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPaginated.repository.QagDateFreezeRepository
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.ceil

@Service
class QagPaginatedUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val dateFreezeRepository: QagDateFreezeRepository,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getPopularQagPaginated(userId: String, pageNumber: Int, thematiqueId: String?): QagsAndMaxPageCount? {
        return getQagPaginated(
            getQagMethod = GetQagMethod.WithoutUserId(QagInfoRepository::getPopularQagsPaginated),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId,
        )
    }

    fun getLatestQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        return getQagPaginated(
            getQagMethod = GetQagMethod.WithoutUserId(QagInfoRepository::getLatestQagsPaginated),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId,
        )
    }

    fun getSupportedQagPaginated(
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        return getQagPaginated(
            getQagMethod = GetQagMethod.WithUserId(QagInfoRepository::getSupportedQagsPaginated),
            userId = userId,
            pageNumber = pageNumber,
            thematiqueId = thematiqueId,
        )
    }

    private fun getQagPaginated(
        getQagMethod: GetQagMethod,
        userId: String,
        pageNumber: Int,
        thematiqueId: String?,
    ): QagsAndMaxPageCount? {
        if (pageNumber <= 0) return null

        val qagsCount = qagInfoRepository.getQagsCount(thematiqueId = thematiqueId)
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > qagsCount) return null

        val dateFreeze = if (pageNumber == 1) {
            dateFreezeRepository.initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        } else {
            dateFreezeRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
        }
        val qags = when (getQagMethod) {
            is GetQagMethod.WithUserId -> getQagMethod.method.invoke(
                qagInfoRepository,
                userId,
                dateFreeze,
                offset,
                thematiqueId,
            )
            is GetQagMethod.WithoutUserId -> getQagMethod.method.invoke(
                qagInfoRepository,
                dateFreeze,
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

data class QagsAndMaxPageCount(
    val qags: List<QagPreview>,
    val maxPageCount: Int,
)

private sealed class GetQagMethod {
    data class WithoutUserId(
        val method: QagInfoRepository.(maxDate: Date, offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>
    ) : GetQagMethod()

    data class WithUserId(
        val method: QagInfoRepository.(userId: String, maxDate: Date, offset: Int, thematiqueId: String?) -> List<QagInfoWithSupportCount>
    ) : GetQagMethod()
}