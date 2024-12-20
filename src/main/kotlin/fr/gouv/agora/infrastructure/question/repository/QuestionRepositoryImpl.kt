package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.Questions
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationDatabaseRepository
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Component

@Component
class QuestionRepositoryImpl(
    private val consultationDatabaseRepository: ConsultationDatabaseRepository,
    private val consultationStrapiRepository: ConsultationStrapiRepository,
    private val questionDatabaseRepository: QuestionDatabaseRepository,
    private val choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val questionMapper: QuestionsMapper,
) : QuestionRepository {

    override fun getConsultationQuestions(consultationId: String): Questions {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            return consultationDatabaseRepository.getConsultation(consultationUUID)?.let { consultationDTO ->
                val questions = questionDatabaseRepository.getQuestionConsultation(consultationUUID) ?: emptyList()
                Questions(
                    questionCount = consultationDTO.questionCountNumber,
                    questions = questions.map { questionDTO ->
                        questionMapper.toDomain(
                            dto = questionDTO,
                            questionDTOList = questions,
                            choixPossibleDTOList = choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionDTO.id)
                                ?: emptyList(),
                        )
                    },
                )
            } ?: Questions(questionCount = 0, questions = emptyList())
        }

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
                ?: return Questions(questionCount = 0, questions = emptyList())

            val questions = questionMapper.toDomain(consultation)

            return Questions(consultation.attributes.nombreDeQuestion, questions)
        }

        return Questions(questionCount = 0, questions = emptyList())
    }
}
