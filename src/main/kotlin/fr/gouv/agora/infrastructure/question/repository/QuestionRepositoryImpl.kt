package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.Questions
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Component

@Component
class QuestionRepositoryImpl(
    private val consultationStrapiRepository: ConsultationStrapiRepository,
    private val questionMapper: QuestionsMapper,
) : QuestionRepository {

    override fun getConsultationQuestions(consultationId: String): Questions {
        val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
            ?: return Questions(questionCount = 0, questions = emptyList())

        val questions = questionMapper.toDomain(consultation)

        return Questions(consultation.attributes.nombreDeQuestion, questions)
    }
}
