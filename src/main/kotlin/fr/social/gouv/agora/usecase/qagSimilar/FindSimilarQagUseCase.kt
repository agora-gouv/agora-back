package fr.social.gouv.agora.usecase.qagSimilar

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagModeratingMapper
import fr.social.gouv.agora.usecase.qagSimilar.repository.VectorizedWordsRepository
import org.springframework.stereotype.Service

@Service
class FindSimilarQagUseCase(
    private val filterGenerator: FindQagSimilarFilterGenerator,
    private val getQagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val sentenceToWordsGenerator: SentenceToWordsGenerator,
    private val vectorizedWordsRepository: VectorizedWordsRepository,
    private val qagSimilarityCalculator: QagSimilarityCalculator,
    private val mapper: QagModeratingMapper,
) {

    companion object {
        private const val MIN_SIMILARITY_SCORE = 75.0
        private const val MAX_QAGS_RETURN = 3
    }

    fun findSimilarQags(writingQag: String, userId: String): List<QagModerating> {
        val qags = getQagListUseCase.getQagWithSupportAndThematique(filterGenerator.getFindQagSimilarFilters())
        if (qags.isEmpty()) return emptyList()

        val qagToWords = qags.associateWith { qag ->
            sentenceToWordsGenerator.sentenceToWords(qag.qagInfo.title)
        }
        val writingQagWords = sentenceToWordsGenerator.sentenceToWords(writingQag)
        val words = (writingQagWords + qagToWords.values.flatten()).distinct()
        val wordVectors = vectorizedWordsRepository.getWordVectors(words)

        println("Compare similarity, writing : $writingQag")
        return qags
            .asSequence()
            .map { qag ->
                val similarityScore = qagSimilarityCalculator.calculateSimilarity(
                    qagWordVectors = wordVectors.filterKeys { word -> qagToWords[qag]?.contains(word) ?: false }
                        .values.filterNotNull().toList(),
                    writingQagWordVectors = wordVectors.filterKeys { word -> writingQagWords.contains(word) }
                        .values.filterNotNull().toList(),
                )

                mapper.toQagModerating(qag = qag, userId = userId) to similarityScore
            }
            .filter { (_, similarityScore) -> similarityScore >= MIN_SIMILARITY_SCORE }
            .sortedByDescending { (_, similarityScore) -> similarityScore }
            .take(MAX_QAGS_RETURN)
            .map { (qag, _) -> qag }
            .toList()
    }

}