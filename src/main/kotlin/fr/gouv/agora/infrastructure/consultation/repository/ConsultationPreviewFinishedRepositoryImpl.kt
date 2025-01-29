package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewFinishedRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewFinishedRepositoryImpl(
    private val strapiRepository: ConsultationStrapiRepository,
    private val mapper: ConsultationInfoMapper,
    private val clock: Clock,
) : ConsultationPreviewFinishedRepository {

    override fun getConsultationFinishedCount(): Int {
        val now = LocalDateTime.now(clock)
        return strapiRepository.countFinishedConsultations(now)
    }

    override fun getConsultationFinishedList(territories: List<Territoire>): List<ConsultationWithUpdateInfo> {
        val now = LocalDateTime.now(clock)
        val strapiConsultationFinished = strapiRepository.getConsultationsFinishedByTerritories(now, territories)
            .let { mapper.toConsultationsWithUpdateInfo(it, now) }

        return strapiConsultationFinished
    }

    override fun getConsultationFinishedList(
        offset: Int,
        pageSize: Int,
        territory: Territoire
    ): List<ConsultationWithUpdateInfo> {
        val now = LocalDateTime.now(clock)
        val strapiConsultationFinished = strapiRepository.getConsultationsFinished(now, territory)
            .let { mapper.toConsultationsWithUpdateInfo(it, now) }

        return strapiConsultationFinished
    }
}
