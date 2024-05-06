package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.Questions
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationDatabaseRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class QuestionRepositoryImpl(
    private val consultationDatabaseRepository: ConsultationDatabaseRepository,
    private val questionDatabaseRepository: QuestionDatabaseRepository,
    private val choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository,
    private val questionMapper: QuestionMapper,
) : QuestionRepository {

    override fun getConsultationQuestions(consultationId: String): Questions {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            consultationDatabaseRepository.getConsultation(consultationUUID)?.let { consultationDTO ->
                Questions(
                    questionCount = consultationDTO.questionCountNumber,
                    questions = getConsultationQuestions(consultationUUID),
                )
            }
        } ?: Questions(questionCount = 0, questions = emptyList())
    }

    override fun getConsultationQuestionList(consultationId: String): List<Question> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            getConsultationQuestions(consultationUUID)
        } ?: emptyList()
    }

    private fun getConsultationQuestions(consultationUUID: UUID): List<Question> {
        val questions = questionDatabaseRepository.getQuestionConsultation(consultationUUID) ?: emptyList()
        return questions.map { questionDTO ->
            questionMapper.toDomain(
                dto = questionDTO,
                questionDTOList = questions,
                choixPossibleDTOList = choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionDTO.id)
                    ?: emptyList(),
            )
        }
    }

}