package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service


@Service
class GetConsultationUseCase(private val repository: ConsultationRepository, private val thematiqueRepository: ThematiqueRepository) {
    fun getConsultation(id: String): Consultation? {
        val consultationInfo = repository.getConsultation(id)!!
        val thematiqueList = thematiqueRepository.getThematiqueList()
        val thematique = thematiqueList.find { it.id == consultationInfo.thematiqueId }!!
        return Consultation(
            id = consultationInfo.id,
            title = consultationInfo.title,
            coverUrl = consultationInfo.coverUrl,
            abstract = consultationInfo.abstract,
            startDate = consultationInfo.startDate,
            endDate = consultationInfo.endDate,
            questionCount = consultationInfo.questionCount,
            estimatedTime = consultationInfo.estimatedTime,
            participantCountGoal = consultationInfo.participantCountGoal,
            description = consultationInfo.description,
            tipsDescription = consultationInfo.tipsDescription,
            thematique = thematique,
        )
    }
}


