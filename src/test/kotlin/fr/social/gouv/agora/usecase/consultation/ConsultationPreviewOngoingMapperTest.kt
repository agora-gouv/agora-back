package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.*
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ConsultationPreviewOngoingMapperTest {

    @Autowired
    private lateinit var mapper: ConsultationPreviewOngoingMapper

    private val consultationPreviewOngoingInfo = ConsultationPreviewOngoingInfo(
        id = "consultationId",
        title = "title",
        coverUrl = "coverUrl",
        thematiqueId = "thematiqueId",
        endDate = Date(1),
    )

    private val thematique = mock(Thematique::class.java)
    private val hasAnswered = true

    private val expectedConsultationPreviewOngoing = ConsultationPreviewOngoing(
        id = "consultationId",
        title = "title",
        coverUrl = "coverUrl",
        endDate = Date(1),
        highlightLabel = null,
        thematique = thematique,
        hasAnswered = hasAnswered,
    )

    companion object {

        @JvmStatic
        fun toConsultationPreviewOngoingCases() = arrayOf(
            input(
                whenTestDescription = "consultationEndDate is more than 15 days away in the future from today",
                serverDate = LocalDate.of(1998, Month.JULY, 12),
                consultationEndDate = LocalDate.of(2012, Month.DECEMBER, 21),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is 15 days in the future from today",
                serverDate = LocalDate.of(2000, Month.JANUARY, 1),
                consultationEndDate = LocalDate.of(2000, Month.JANUARY, 16),
                expectedHighlightLabel = "Plus que 15 jours",
            ),
            input(
                whenTestDescription = "consultationEndDate is 1 days from today",
                serverDate = LocalDate.of(2000, Month.JANUARY, 1),
                consultationEndDate = LocalDate.of(2000, Month.JANUARY, 2),
                expectedHighlightLabel = "Dernier jour !",
            ),
            input(
                whenTestDescription = "consultationEndDate is today",
                serverDate = LocalDate.of(2000, Month.JANUARY, 1),
                consultationEndDate = LocalDate.of(2020, Month.JANUARY, 1),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is in the near past",
                serverDate = LocalDate.of(2000, Month.JANUARY, 1),
                consultationEndDate = LocalDate.of(1999, Month.DECEMBER, 25),
                expectedHighlightLabel = null,
            ),
            input(
                whenTestDescription = "consultationEndDate is very far away in the past",
                serverDate = LocalDate.of(2000, Month.JANUARY, 1),
                consultationEndDate = LocalDate.of(1989, Month.JANUARY, 1),
                expectedHighlightLabel = null,
            ),
        )

        private fun input(
            whenTestDescription: String,
            serverDate: LocalDate,
            consultationEndDate: LocalDate,
            expectedHighlightLabel: String?,
        ) = arrayOf(whenTestDescription, serverDate, consultationEndDate, expectedHighlightLabel)

    }

    @ParameterizedTest(name = "toConsultationPreviewOngoing - when {0} - should return expected")
    @MethodSource("toConsultationPreviewOngoingCases")
    fun `toConsultationPreviewOngoing - should return expected`(
        whenTestDescription: String,
        serverDate: LocalDate,
        consultationEndDate: LocalDate,
        expectedHighlightLabel: String?,
    ) {
        setupNowDate(serverDate)
        // Given
        val endDate = consultationEndDate.toDate()
        val consultationPreviewOngoingInfo = consultationPreviewOngoingInfo.copy(endDate = endDate)

        // When
        val result = mapper.toConsultationPreviewOngoing(
            consultationPreviewOngoingInfo = consultationPreviewOngoingInfo,
            thematique = thematique,
            hasAnswered = hasAnswered,
        )

        // Then
        assertThat(result).isEqualTo(
            expectedConsultationPreviewOngoing.copy(
                endDate = endDate,
                highlightLabel = expectedHighlightLabel,
            )
        )
    }

    private fun setupNowDate(date: LocalDate) {
        mapper = ConsultationPreviewOngoingMapper(
            clock = Clock.fixed(date.toDate().toInstant(), ZoneId.systemDefault()),
        )
    }

}