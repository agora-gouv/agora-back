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
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildDataMap(choiceDemographicInfo, questionDemographicInfo) { it.genderCount },
            ageRangeCount = buildDataMap(choiceDemographicInfo, questionDemographicInfo) { it.ageRangeCount },
            departmentCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo
            ) { it.departmentCount },
            cityTypeCount = buildDataMap(choiceDemographicInfo, questionDemographicInfo) { it.cityTypeCount },
            jobCategoryCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo
            ) { it.jobCategoryCount },
            voteFrequencyCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo
            ) { it.voteFrequencyCount },
            publicMeetingFrequencyCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
            ) { it.publicMeetingFrequencyCount },
            consultationFrequencyCount = buildDataMap(
                choiceDemographicInfo,
                questionDemographicInfo,
            ) { it.consultationFrequencyCount },
        )
    }

    private fun <K> buildDataMap(
        demographicInfoCount: DemographicInfoCount,
        questionDemographicInfo: DemographicInfoCountByChoices,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>,
    ): Map<K?, CountAndRatio> {
        val participantCountPerKey = questionDemographicInfo.choiceDemographicInfoMap.values.fold(
            initial = emptyMap<K?, Int>(),
        ) { participantCountPerKey, demographicInfo ->
            val demographicInfoMap = keySelector.invoke(demographicInfo)

            val updatingParticipantCountPerKey = participantCountPerKey.toMutableMap()
            demographicInfoMap.entries.forEach { (key, count) ->
                updatingParticipantCountPerKey[key] = (updatingParticipantCountPerKey[key] ?: 0) + count
            }
            updatingParticipantCountPerKey.toMap()
        }

        val demographicInfoMap = keySelector.invoke(demographicInfoCount)
        return demographicInfoMap.map { (key, count) ->
            key to getCountAndRatio(
                count = count,
                allCount = participantCountPerKey[key] ?: 0,
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