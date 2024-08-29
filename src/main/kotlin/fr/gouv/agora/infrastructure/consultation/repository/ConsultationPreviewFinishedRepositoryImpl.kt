package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedRepositoryImpl(
    private val databaseRepository: ConsultationDatabaseRepository,
    private val mapper: ConsultationInfoMapper,
) : ConsultationPreviewFinishedRepository {

    override fun getConsultationFinishedCount(): Int {
        return databaseRepository.getConsultationFinishedCount()
    }

    override fun getConsultationFinishedList(offset: Int): List<ConsultationWithUpdateInfo> {
        return databaseRepository.getConsultationsFinishedWithUpdateInfo(offset)
            .map(mapper::toConsultationWithUpdateInfo)
    }

}
