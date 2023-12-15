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
        val demographicInfoByChoices =
            consultationResponseRepository.getParticipantDemographicInfoByChoices(consultationId = consultationId)

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
                        demographicInfoByChoices = demographicInfoByChoices,
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
        demographicInfoByChoices: DemographicInfoCountByChoices,
    ) = QuestionResultWithDemographicInfo(
        question = question,
        responses = question.choixPossibleList.map { choix ->
            buildChoiceResult(
                question = question,
                choix = choix,
                participantCount = participantCount,
                consultationResponseList = consultationResponseList,
                demographicInfoCount = demographicInfoByChoices.choiceDemographicInfoMap[choix.id]
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
                questionDemographicInfoCount = question.choixPossibleList.mapNotNull { otherChoice ->
                    demographicInfoByChoices.choiceDemographicInfoMap[otherChoice.id]
                },
            )
        }
    )

    private fun buildChoiceResult(
        question: Question,
        choix: ChoixPossible,
        participantCount: Int,
        consultationResponseList: List<ResponseConsultationCount>,
        demographicInfoCount: DemographicInfoCount,
        questionDemographicInfoCount: List<DemographicInfoCount>,
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
                demographicInfoCount = demographicInfoCount,
                questionDemographicInfoCount = questionDemographicInfoCount,
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
        demographicInfoCount: DemographicInfoCount,
        questionDemographicInfoCount: List<DemographicInfoCount>,
    ): DemographicInfo {
        return DemographicInfo(
            genderCount = buildDataMap(demographicInfoCount, questionDemographicInfoCount) { it.genderCount },
            ageRangeCount = buildDataMap(demographicInfoCount, questionDemographicInfoCount) { it.ageRangeCount },
            departmentCount = buildDataMap(
                demographicInfoCount,
                questionDemographicInfoCount
            ) { it.departmentCount },
            cityTypeCount = buildDataMap(demographicInfoCount, questionDemographicInfoCount) { it.cityTypeCount },
            jobCategoryCount = buildDataMap(
                demographicInfoCount,
                questionDemographicInfoCount
            ) { it.jobCategoryCount },
            voteFrequencyCount = buildDataMap(
                demographicInfoCount,
                questionDemographicInfoCount
            ) { it.voteFrequencyCount },
            publicMeetingFrequencyCount = buildDataMap(
                demographicInfoCount,
                questionDemographicInfoCount,
            ) { it.publicMeetingFrequencyCount },
            consultationFrequencyCount = buildDataMap(
                demographicInfoCount,
                questionDemographicInfoCount,
            ) { it.consultationFrequencyCount },
        )
    }

    private fun <K> buildDataMap(
        demographicInfoCount: DemographicInfoCount,
        questionDemographicInfoCount: List<DemographicInfoCount>,
        keySelector: (DemographicInfoCount) -> Map<K?, Int>,
    ): Map<K?, CountAndRatio> {
        val participantCountPerKey = questionDemographicInfoCount.fold(
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