package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.domain.ChoiceResultWithDemographicInfo
import fr.gouv.agora.domain.ChoixPossible
import fr.gouv.agora.domain.ConsultationResultWithDemographicInfo
import fr.gouv.agora.domain.CountAndRatio
import fr.gouv.agora.domain.DemographicInfo
import fr.gouv.agora.domain.DemographicInfoCount
import fr.gouv.agora.domain.DemographicInfoCountByChoices
import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionResultWithDemographicInfo
import fr.gouv.agora.domain.QuestionWithChoices
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationResults.QuestionNoResponseMapper
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsWithDemographicRatiosUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val mapper: QuestionNoResponseMapper,
) {
    private val logger: Logger = LoggerFactory.getLogger(GetConsultationResultsWithDemographicRatiosUseCase::class.java)

    fun getConsultationResults(consultationId: String): ConsultationResultWithDemographicInfo? {
        logger.info("📊 Building consultation results for consultationId: $consultationId...")
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId = consultationId)
            ?: return null

        val questionList = questionRepository.getConsultationQuestions(consultationId).questions
            .filterIsInstance<QuestionWithChoices>()
        val participantCount = userAnsweredConsultationRepository.getParticipantCount(consultationId = consultationId)
        val demographicInfo =
            consultationResponseRepository.getParticipantDemographicInfo(consultationId = consultationId)
        val consultationResponseList =
            consultationResponseRepository.getConsultationResponsesCount(consultationId)

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
                        totalDemographicInfo = demographicInfo,
                    )
                },
            demographicInfo = buildDemographicInfo(
                demographicInfoCount = demographicInfo,
                participantCount = participantCount,
            ),
        )
    }

    private fun buildQuestionResults(
        question: QuestionWithChoices,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
        totalDemographicInfo: DemographicInfoCount,
    ): QuestionResultWithDemographicInfo {
        val questionDemographicInfo =
            consultationResponseRepository.getParticipantDemographicInfoByChoices(questionId = question.id)
        return QuestionResultWithDemographicInfo(
            question = question,
            responses = question.choixPossibleList.map { choix ->
                buildChoiceResult(
                    question = question,
                    choix = choix,
                    participantCount = participantCount,
                    consultationResponseList = consultationResponseList,
                    questionDemographicInfo = questionDemographicInfo,
                    totalDemographicInfo = totalDemographicInfo,
                )
            }
        )
    }

    private fun buildChoiceResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
        questionDemographicInfo: DemographicInfoCountByChoices,
        totalDemographicInfo: DemographicInfoCount,
    ): ChoiceResultWithDemographicInfo {
        val choiceCount = consultationResponseList
            .filter { it.questionId == question.id && it.choiceId == choix.id }
            .sumOf { it.responseCount }

        return ChoiceResultWithDemographicInfo(
            choixPossible = choix,
            countAndRatio = getCountAndRatio(
                count = choiceCount,
                allCount = participantCount,
            ),
            demographicInfo = buildChoicesDemographicInfo(
                choiceDemographicInfo = questionDemographicInfo.choiceDemographicInfoMap[choix.id]
                    ?: DemographicInfoCount(
                        genderCount = emptyMap(),
                        ageRangeCount = emptyMap(),
                        departmentCount = emptyMap(),
                        cityTypeCount = emptyMap(),
                        jobCategoryCount = emptyMap(),
                        voteFrequencyCount = emptyMap(),
                        publicMeetingFrequencyCount = emptyMap(),
                        consultationFrequencyCount = emptyMap(),
                    ),
                totalDemographicInfo = totalDemographicInfo,
                choiceCount = choiceCount,
            ),
        )
    }

    private fun buildDemographicInfo(
        demographicInfoCount: DemographicInfoCount,
        participantCount: Int,
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildDataMap(demographicInfoCount, participantCount) { it.genderCount },
            ageRangeCount = buildDataMap(demographicInfoCount, participantCount) { it.ageRangeCount },
            departmentCount = buildDataMap(demographicInfoCount, participantCount) { it.departmentCount },
            cityTypeCount = buildDataMap(demographicInfoCount, participantCount) { it.cityTypeCount },
            jobCategoryCount = buildDataMap(demographicInfoCount, participantCount) { it.jobCategoryCount },
            voteFrequencyCount = buildDataMap(demographicInfoCount, participantCount) { it.voteFrequencyCount },
            publicMeetingFrequencyCount = buildDataMap(
                demographicInfoCount = demographicInfoCount,
                participantCount = participantCount
            ) { it.publicMeetingFrequencyCount },
            consultationFrequencyCount = buildDataMap(
                demographicInfoCount = demographicInfoCount,
                participantCount = participantCount
            ) { it.consultationFrequencyCount },
        )
    }

    private fun <K> buildDataMap(
        demographicInfoCount: DemographicInfoCount,
        participantCount: Int,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>,
    ): Map<K?, CountAndRatio> {
        val demographicInfoMap = keySelector.invoke(demographicInfoCount)
        return demographicInfoMap.map { (key, count) ->
            key to getCountAndRatio(
                count = if (key == null) {
                    val nonNullCount = demographicInfoMap.entries.sumOf { (key, count) -> key?.let { count } ?: 0 }
                    participantCount - nonNullCount
                } else count,
                allCount = participantCount,
            )
        }.toMap()
    }

    private fun buildChoicesDemographicInfo(
        choiceDemographicInfo: DemographicInfoCount,
        totalDemographicInfo: DemographicInfoCount,
        choiceCount: Int,
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.genderCount },
            ageRangeCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.ageRangeCount },
            departmentCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.departmentCount },
            cityTypeCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.cityTypeCount },
            jobCategoryCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.jobCategoryCount },
            voteFrequencyCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.voteFrequencyCount },
            publicMeetingFrequencyCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.publicMeetingFrequencyCount },
            consultationFrequencyCount = buildChoicesDataMap(
                choiceDemographicInfo,
                totalDemographicInfo,
                choiceCount,
            ) { it.consultationFrequencyCount },
        )
    }

    private fun <K> buildChoicesDataMap(
        choiceDemographicInfoCount: DemographicInfoCount,
        totalDemographicInfoCount: DemographicInfoCount,
        choiceCount: Int,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>,
    ): Map<K?, CountAndRatio> {
        val demographicInfoMap = keySelector.invoke(choiceDemographicInfoCount)
        val totalDemographicInfoMap = keySelector.invoke(totalDemographicInfoCount)
        return demographicInfoMap.map { (key, count) ->
            key to getCountAndRatio(
                count = if (key == null) {
                    val nonNullCount = demographicInfoMap.entries.sumOf { (key, count) -> key?.let { count } ?: 0 }
                    choiceCount - nonNullCount
                } else count,
                allCount = totalDemographicInfoMap[key] ?: 0,
            )
        }.toMap()
    }

    private fun getCountAndRatio(count: Int, allCount: Int): CountAndRatio {
        return CountAndRatio(
            count = count,
            ratio = (count.toDouble() / allCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }

}
