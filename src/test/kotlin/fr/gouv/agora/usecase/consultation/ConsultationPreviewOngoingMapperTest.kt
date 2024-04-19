package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ConsultationPreviewOngoingMapperTest {

    private lateinit var mapper: ConsultationPreviewOngoingMapper

    private val thematique = mock(Thematique::class.java)

    private val expectedConsultationPreviewOngoing = ConsultationPreviewOngoing(
        id = "consultationId",
        title = "title",
        coverUrl = "coverUrl",
        thematique = thematique,
        endDate = Date(1),
        highlightLabel = null,
    )

    companion object {
        @JvmStatic
        fun toConsultationPreviewOngoingCases() = arrayOf(
            input(
                whenTestDescription = "consultationEndDate is more than 7 days away in the future from today",
                serverDate = LocalDateTime.of(1998, Month.JULY, 12, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2012, Month.DECEMBER, 21, 16, 45, 0),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is 7 days in the future from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 8, 16, 45, 0),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is 6 days in the future from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 7, 16, 45, 0),
                expectedHighlightLabel = "Plus que 7 jours !",
            ),
            input(
                whenTestDescription = "consultationEndDate is 5 days in the future from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 6, 16, 45, 0),
                expectedHighlightLabel = "Plus que 6 jours !",
            ),
            input(
                whenTestDescription = "consultationEndDate is 4 days in the future from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 5, 16, 45, 0),
                expectedHighlightLabel = "Plus que 5 jours !",
            ),
            input(
                whenTestDescription = "consultationEndDate is 3 days in the future from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 4, 16, 45, 0),
                expectedHighlightLabel = "Plus que 4 jours !",
            ),
            input(
                whenTestDescription = "consultationEndDate is 2 days in the future from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 3, 16, 45, 0),
                expectedHighlightLabel = "Plus que 3 jours !",
            ),
            input(
                whenTestDescription = "consultationEndDate is 1 days from today",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 2, 16, 45, 0),
                expectedHighlightLabel = "Plus que 2 jours !",
            ),
            input(
                whenTestDescription = "consultationEndDate is today before hour",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 1, 16, 45, 0),
                expectedHighlightLabel = "Dernier jour !",
            ),
            input(
                whenTestDescription = "consultationEndDate is today at same hour",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 16, 45, 0),
                consultationEndDate = LocalDateTime.of(2000, Month.JANUARY, 1, 16, 45, 0),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is in the near past",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(1999, Month.DECEMBER, 25, 16, 45, 0),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is very far away in the past",
                serverDate = LocalDateTime.of(2000, Month.JANUARY, 1, 9, 30, 0),
                consultationEndDate = LocalDateTime.of(1989, Month.JANUARY, 1, 16, 45, 0),
                expectedHighlightLabel = null,
            ),
        )

        private fun input(
            whenTestDescription: String,
            serverDate: LocalDateTime,
            consultationEndDate: LocalDateTime,
            expectedHighlightLabel: String?,
        ) = arrayOf(whenTestDescription, serverDate, consultationEndDate, expectedHighlightLabel)

    }

    @ParameterizedTest(name = "toConsultationPreviewOngoing - when {0} - should return expected")
    @MethodSource("toConsultationPreviewOngoingCases")
    fun `toConsultationPreviewOngoing - should return expected`(
        whenTestDescription: String,
        serverDate: LocalDateTime,
        consultationEndDate: LocalDateTime,
        expectedHighlightLabel: String?,
    ) {
        // Given
        mapper = ConsultationPreviewOngoingMapper(clock = TestUtils.getFixedClock(serverDate))
        val endDate = consultationEndDate.toDate()
        val consultationInfo = mockConsultationInfo(endDate = endDate)

        // When
        val result = mapper.toConsultationPreviewOngoing(
            consultationInfo = consultationInfo,
            thematique = thematique,
        )

        // Then
        assertThat(result).isEqualTo(
            expectedConsultationPreviewOngoing.copy(
                endDate = endDate,
                highlightLabel = expectedHighlightLabel,
            )
        )
    }

    private fun mockConsultationInfo(endDate: Date): ConsultationInfo {
        return mock(ConsultationInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.endDate).willReturn(endDate)
        }
    }

}