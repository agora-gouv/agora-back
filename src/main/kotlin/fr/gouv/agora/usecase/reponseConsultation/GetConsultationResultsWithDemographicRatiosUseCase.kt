package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsWithDemographicRatiosUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val mapper: QuestionNoResponseMapper,
) {

    fun getConsultationResults(consultationId: String): ConsultationResultWithDemographicInfo? {
        println("📊 Building consultation results for consultationId: $consultationId...")
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId = consultationId)
            ?: return null

        val questionList =
            questionRepository.getConsultationQuestionList(consultationId).filterIsInstance<QuestionWithChoices>()
        val participantCount = consultationResponseRepository.getParticipantCount(consultationId = consultationId)
        val consultationResponseList =
            consultationResponseRepository.getConsultationResponsesCountWithDemographicInfo(consultationId)

        return ConsultationResultWithDemographicInfo(
            consultation = consultationInfo,
            participantCount = participantCount,
            results = questionList
                .filter { question -> question.choixPossibleList.isNotEmpty() }
                .map { question -> mapper.toQuestionNoResponse(question) }
                .map { question ->
                    buildQuestionResults(
                        question = question,
                        participantCount = participantCount,
                        consultationResponseList = consultationResponseList,
                    )
                },
            demographicInfo = buildDemographicInfo(
                responses = consultationResponseList,
                participantCount = participantCount,
            ),
        )
    }

    private fun buildQuestionResults(
        question: QuestionWithChoices,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCountWithDemographicInfo>,
    ) = QuestionResultWithDemographicInfo(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildChoiceResult(
                question = question,
                choix = choix,
                participantCount = participantCount,
                consultationResponseList = consultationResponseList,
            )
        }
    )

    private fun buildChoiceResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCountWithDemographicInfo>,
    ): ChoiceResultWithDemographicInfo {
        val choixResponses = consultationResponseList.filter { it.questionId == question.id && it.choiceId == choix.id }

        return ChoiceResultWithDemographicInfo(
            choixPossible = choix,
            countAndRatio = getCountAndRatio(
                count = choixResponses.sumOf { it.responseCount },
                allCount = participantCount,
            ),
            demographicInfo = buildDemographicInfo(
                responses = choixResponses,
                participantCount = choixResponses.size,
            ),
        )
    }

    private fun buildDemographicInfo(
        responses: List<ResponseConsultationCountWithDemographicInfo>,
        participantCount: Int,
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildDataMap(responses, participantCount) { response -> response?.gender },
            yearOfBirthCount = buildDataMap(responses, participantCount) { response -> response?.yearOfBirth },
            departmentCount = buildDataMap(responses, participantCount) { response -> response?.department },
            cityTypeCount = buildDataMap(responses, participantCount) { response -> response?.cityType },
            jobCategoryCount = buildDataMap(responses, participantCount) { response -> response?.jobCategory },
            voteFrequencyCount = buildDataMap(responses, participantCount) { response -> response?.voteFrequency },
            publicMeetingFrequencyCount = buildDataMap(
                responses,
                participantCount,
            ) { response -> response?.publicMeetingFrequency },
            consultationFrequencyCount = buildDataMap(
                responses,
                participantCount,
            ) { response -> response?.consultationFrequency },
        )
    }

    private fun <K> buildDataMap(
        consultationResponseList: List<ResponseConsultationCountWithDemographicInfo>,
        participantCount: Int,
        keySelector: (ResponseConsultationCountWithDemographicInfo?) -> K,
    ): Map<K, CountAndRatio> {
        return consultationResponseList
            .groupBy(keySelector)
            .map { (key: K, profiles) ->
                key to getCountAndRatio(
                    count = profiles.sumOf { it.responseCount },
                    allCount = participantCount,
                )
            }
            .toMap()
    }

    private fun getCountAndRatio(count: Int, allCount: Int): CountAndRatio {
        return CountAndRatio(
            count = count,
            ratio = (count.toDouble() / allCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }

}