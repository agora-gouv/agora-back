package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.question.repository.QuestionRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationResultsWithDemographicRatiosUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val questionRepository: QuestionRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val mapper: QuestionNoResponseMapper,
) {

    fun getConsultationResults(consultationId: String): ConsultationResultWithDemographicInfo? {
        println("📊 Building consultation results for consultationId: $consultationId...")
        val consultationInfo = consultationInfoRepository.getConsultation(consultationId = consultationId)
            ?: return null

        val questionList =
            questionRepository.getConsultationQuestionList(consultationId).filterIsInstance<QuestionWithChoices>()
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
                    questionCount = consultationResponseList.filter { it.questionId == question.id }
                        .sumOf { it.responseCount },
                    consultationResponseList = consultationResponseList,
                    questionDemographicInfo = questionDemographicInfo,
                )
            }
        )
    }

    private fun buildChoiceResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        questionCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
        questionDemographicInfo: DemographicInfoCountByChoices,
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
                questionDemographicInfo = questionDemographicInfo,
                questionCount = questionCount,
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
        questionDemographicInfo: DemographicInfoCountByChoices,
        questionCount: Int,
        choiceCount: Int,
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.genderCount },
            ageRangeCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.ageRangeCount },
            departmentCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.departmentCount },
            cityTypeCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.cityTypeCount },
            jobCategoryCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.jobCategoryCount },
            voteFrequencyCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.voteFrequencyCount },
            publicMeetingFrequencyCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.publicMeetingFrequencyCount },
            consultationFrequencyCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
                questionCount,
                choiceCount,
            ) { it.consultationFrequencyCount },
        )
    }

    private fun <K> buildDataMap(
        demographicInfoCount: DemographicInfoCount,
        questionDemographicInfo: DemographicInfoCountByChoices,
        questionCount: Int,
        choiceCount: Int,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>,
    ): Map<K?, CountAndRatio> {
        val demographicInfoMap = keySelector.invoke(demographicInfoCount)
        return demographicInfoMap.map { (key, count) ->
            key to getCountAndRatio(
                count = if (key == null) {
                    val nonNullCount = demographicInfoMap.entries.sumOf { (key, count) -> key?.let { count } ?: 0 }
                    choiceCount - nonNullCount
                } else count,
                allCount = if (key == null) {
                    buildNAQuestionDemographicKeyCount<K>(questionCount, questionDemographicInfo, keySelector)
                } else {
                    buildQuestionDemographicKeyCount(questionDemographicInfo, keySelector, key)
                }
            )
        }.toMap()
    }

    private fun <K> buildQuestionDemographicKeyCount(
        questionDemographicInfo: DemographicInfoCountByChoices,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>,
        key: K
    ) = questionDemographicInfo.choiceDemographicInfoMap.values.fold(
        initial = 0,
    ) { demographicInfoQuestionCount, demographicInfo ->
        demographicInfoQuestionCount + (keySelector.invoke(demographicInfo)[key] ?: 0)
    }

    private fun <K> buildNAQuestionDemographicKeyCount(
        questionCount: Int,
        questionDemographicInfo: DemographicInfoCountByChoices,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>
    ) = questionCount - questionDemographicInfo.choiceDemographicInfoMap.values.fold(
        initial = 0,
    ) { demographicInfoQuestionCount, demographicInfo ->
        demographicInfoQuestionCount + (keySelector.invoke(demographicInfo).entries.sumOf { (key, count) ->
            count.takeIf { key != null } ?: 0
        })
    }

    private fun getCountAndRatio(count: Int, allCount: Int): CountAndRatio {
        return CountAndRatio(
            count = count,
            ratio = (count.toDouble() / allCount).takeUnless { it.isNaN() } ?: 0.0,
        )
    }

}