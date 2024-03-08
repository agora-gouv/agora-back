package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.repository

import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class FeedbackConsultationUpdateMapperTest {

    @Autowired
    private lateinit var mapper: FeedbackConsultationUpdateMapper

    companion object {
        @JvmStatic
        fun toStatsTestCases() = arrayOf(
            input(
                positiveCount = 0,
                negativeCount = 0,
                expectedPositiveRatio = 0,
                expectedNegativeRatio = 0,
            ),
            input(
                positiveCount = 3,
                negativeCount = 1,
                expectedPositiveRatio = 75,
                expectedNegativeRatio = 25,
            ),
            input(
                positiveCount = 2,
                negativeCount = 1,
                expectedPositiveRatio = 67,
                expectedNegativeRatio = 33,
            ),
            input(
                positiveCount = 78,
                negativeCount = 22,
                expectedPositiveRatio = 78,
                expectedNegativeRatio = 22,
            ),
        )

        private fun input(
            positiveCount: Int,
            negativeCount: Int,
            expectedPositiveRatio: Int,
            expectedNegativeRatio: Int,
        ) = arrayOf(positiveCount, negativeCount, expectedPositiveRatio, expectedNegativeRatio)
    }

    @MethodSource("toStatsTestCases")
    @ParameterizedTest(name = "toStatsTestCases - when has {0} positive and {1} negative - should return {2}% positive and {3}% negative")
    fun `toStatsTestCases - should return expected ratios`(
        positiveCount: Int,
        negativeCount: Int,
        expectedPositiveRatio: Int,
        expectedNegativeRatio: Int,
    ) {
        // Given
        val rawStats = mapOf(
            0 to negativeCount,
            1 to positiveCount,
        )

        // When
        val result = mapper.toStats(rawStats)

        // Then
        assertThat(result).isEqualTo(
            FeedbackConsultationUpdateStats(
                positiveRatio = expectedPositiveRatio,
                negativeRatio = expectedNegativeRatio,
                responseCount = positiveCount + negativeCount,
            )
        )
    }

    @Test
    fun `toStatsTestCases - when has unknown keys - should ignore them and return expected`() {
        // Given
        val rawStats = mapOf(
            42 to 1337,
        )

        // When
        val result = mapper.toStats(rawStats)

        // Then
        assertThat(result).isEqualTo(
            FeedbackConsultationUpdateStats(
                positiveRatio = 0,
                negativeRatio = 0,
                responseCount = 0,
            )
        )
    }

}