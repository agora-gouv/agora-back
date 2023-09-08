package fr.social.gouv.agora.usecase.qagSimilar

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagFilters
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import fr.social.gouv.agora.usecase.qag.QagModeratingMapper
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qagSimilar.repository.VectorizedWordsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class FindSimilarQagUseCaseTest {

    @Autowired
    private lateinit var useCase: FindSimilarQagUseCase

    @MockBean
    private lateinit var filterGenerator: FindQagSimilarFilterGenerator

    @MockBean
    private lateinit var getQagListUseCase: GetQagWithSupportAndThematiqueUseCase

    @MockBean
    private lateinit var sentenceToWordsGenerator: SentenceToWordsGenerator

    @MockBean
    private lateinit var vectorizedWordsRepository: VectorizedWordsRepository

    @MockBean
    private lateinit var qagSimilarityCalculator: QagSimilarityCalculator

    @MockBean
    private lateinit var mapper: QagModeratingMapper

    private val qagFilter = mock(QagFilters::class.java)

    @BeforeEach
    fun setUp() {
        given(filterGenerator.getFindQagSimilarFilters()).willReturn(qagFilter)
    }

    @Test
    fun `findSimilarQags - when has no qags from qagListUseCase - should return emptyList`() {
        // Given
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilter)).willReturn(emptyList())

        // When
        val result = useCase.findSimilarQags(writingQag = "writing...", userId = "toto")

        // Then
        assertThat(result).isEqualTo(emptyList<QagModerating>())
    }

    @Test
    fun `findSimilarQags - when has qag but similarity score is strictly lower than 75 - should return emptyList`() {
        // Given
        val qag = mockQag(qagTitle = "Hello there !")
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilter)).willReturn(listOf(qag))

        val qagWords = listOf("Hello", "there", "!")
        val writingQagWords = listOf("writing", "...")
        given(sentenceToWordsGenerator.sentenceToWords("Hello there !")).willReturn(qagWords)
        given(sentenceToWordsGenerator.sentenceToWords("writing...")).willReturn(writingQagWords)

        val allWords = writingQagWords + qagWords
        val wordVectors = allWords.associateWith { mock(INDArray::class.java) }
        given(vectorizedWordsRepository.getWordVectors(allWords)).willReturn(wordVectors)

        given(
            qagSimilarityCalculator.calculateSimilarity(
                qagWordVectors = wordVectors.filter { (word, _) -> qagWords.contains(word) }.values.toList(),
                writingQagWordVectors = wordVectors.filter { (word, _) -> writingQagWords.contains(word) }.values.toList(),
            )
        ).willReturn(50.23)

        // When
        val result = useCase.findSimilarQags(writingQag = "writing...", userId = "toto")

        // Then
        assertThat(result).isEqualTo(emptyList<QagModerating>())
    }

    @Test
    fun `findSimilarQags - when has qag but similarity score is higher or equals 75 - should return mapped qag`() {
        // Given
        val qag = mockQag(qagTitle = "Walter write ;)")
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilter)).willReturn(listOf(qag))

        val qagWords = listOf("Walter", "write", ";)")
        val writingQagWords = listOf("Walter", "writing", "...")
        given(sentenceToWordsGenerator.sentenceToWords("Walter write ;)")).willReturn(qagWords)
        given(sentenceToWordsGenerator.sentenceToWords("Walter writing...")).willReturn(writingQagWords)

        val allWords = listOf("Walter", "writing", "...", "write", ";)")
        val wordVectors = allWords.associateWith { mock(INDArray::class.java) }
        given(vectorizedWordsRepository.getWordVectors(allWords)).willReturn(wordVectors)

        given(
            qagSimilarityCalculator.calculateSimilarity(
                qagWordVectors = listOf(wordVectors["Walter"]!!, wordVectors["write"]!!, wordVectors[";)"]!!),
                writingQagWordVectors = listOf(wordVectors["Walter"]!!, wordVectors["writing"]!!, wordVectors["..."]!!),
            )
        ).willReturn(82.17)

        val qagModerating = mock(QagModerating::class.java)
        given(mapper.toQagModerating(qag = qag, userId = "toto")).willReturn(qagModerating)

        // When
        val result = useCase.findSimilarQags(writingQag = "Walter writing...", userId = "toto")

        // Then
        assertThat(result).isEqualTo(listOf(qagModerating))
    }

    @Test
    fun `findSimilarQags - when has has more than 3 qags - should return 3 with highest scores sorted descending by score`() {
        // Given
        val qag1 = mockQag(qagTitle = "1")
        val qag2 = mockQag(qagTitle = "2")
        val qag3 = mockQag(qagTitle = "3")
        val qag4 = mockQag(qagTitle = "4")
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilter)).willReturn(
            listOf(qag1, qag2, qag3, qag4)
        )

        val qagWords = listOf("1", "2", "3", "4")
        val writingQagWords = listOf("0")
        given(sentenceToWordsGenerator.sentenceToWords("0")).willReturn(listOf("0"))
        given(sentenceToWordsGenerator.sentenceToWords("1")).willReturn(listOf("1"))
        given(sentenceToWordsGenerator.sentenceToWords("2")).willReturn(listOf("2"))
        given(sentenceToWordsGenerator.sentenceToWords("3")).willReturn(listOf("3"))
        given(sentenceToWordsGenerator.sentenceToWords("4")).willReturn(listOf("4"))

        val allWords = writingQagWords + qagWords
        val wordVectors = allWords.associateWith { mock(INDArray::class.java) }
        given(vectorizedWordsRepository.getWordVectors(allWords)).willReturn(wordVectors)

        given(
            qagSimilarityCalculator.calculateSimilarity(
                qagWordVectors = listOf(wordVectors["1"]!!),
                writingQagWordVectors = listOf(wordVectors["0"]!!),
            )
        ).willReturn(81.11)
        given(
            qagSimilarityCalculator.calculateSimilarity(
                qagWordVectors = listOf(wordVectors["2"]!!),
                writingQagWordVectors = listOf(wordVectors["0"]!!),
            )
        ).willReturn(82.22)
        given(
            qagSimilarityCalculator.calculateSimilarity(
                qagWordVectors = listOf(wordVectors["3"]!!),
                writingQagWordVectors = listOf(wordVectors["0"]!!),
            )
        ).willReturn(83.33)
        given(
            qagSimilarityCalculator.calculateSimilarity(
                qagWordVectors = listOf(wordVectors["4"]!!),
                writingQagWordVectors = listOf(wordVectors["0"]!!),
            )
        ).willReturn(84.44)

        val qagModerating4 = mock(QagModerating::class.java)
        given(mapper.toQagModerating(qag = qag4, userId = "toto")).willReturn(qagModerating4)

        val qagModerating3 = mock(QagModerating::class.java)
        given(mapper.toQagModerating(qag = qag3, userId = "toto")).willReturn(qagModerating3)

        val qagModerating2 = mock(QagModerating::class.java)
        given(mapper.toQagModerating(qag = qag2, userId = "toto")).willReturn(qagModerating2)

        // When
        val result = useCase.findSimilarQags(writingQag = "0", userId = "toto")

        // Then
        assertThat(result).isEqualTo(listOf(qagModerating4, qagModerating3, qagModerating2))
    }

    private fun mockQag(qagTitle: String): QagInfoWithSupportAndThematique {
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.title).willReturn(qagTitle)
        }

        val qag = mock(QagInfoWithSupportAndThematique::class.java).also {
            given(it.qagInfo).willReturn(qagInfo)
        }

        return qag
    }

}