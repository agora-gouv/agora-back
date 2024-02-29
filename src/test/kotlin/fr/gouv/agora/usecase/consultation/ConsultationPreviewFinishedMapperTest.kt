package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.ConsultationPreviewAnswered
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.Month

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ConsultationPreviewFinishedMapperTest {

    private lateinit var mapper: ConsultationPreviewFinishedMapper

    companion object {
        @JvmStatic
        fun updateLabelTestCases() = arrayOf(
            input(
                testName = "when updateLabel is null - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateLabel = null,
                expectedUpdateLabel = null,
            ),
            input(
                testName = "when serverDate < updateDate - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                updateLabel = "New update !",
                expectedUpdateLabel = null,
            ),
            input(
                testName = "when updateDate < serverDate < updateDate+14d - should return updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 7, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateLabel = "New update !",
                expectedUpdateLabel = "New update !",
            ),
            input(
                testName = "when updateDate < updateDate+14d < serverDate - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 20, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateLabel = "New update !",
                expectedUpdateLabel = null,
            ),
            input(
                testName = "when updateDate = serverDate - should return updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateLabel = "New update !",
                expectedUpdateLabel = "New update !",
            ),
            input(
                testName = "when updateDate+14d = serverDate - should return updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 15, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateLabel = "New update !",
                expectedUpdateLabel = "New update !",
            ),
            input(
                testName = "when updateDate+14d + 1s = serverDate - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 15, 12, 30, 1),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateLabel = "New update !",
                expectedUpdateLabel = null,
            ),
        )

        private fun input(
            testName: String,
            serverDate: LocalDateTime,
            updateDate: LocalDateTime,
            updateLabel: String?,
            expectedUpdateLabel: String?,
        ) = arrayOf(testName, serverDate, updateDate, updateLabel, expectedUpdateLabel)
    }

    @ParameterizedTest(name = "toConsultationPreviewFinished - {0}")
    @MethodSource("updateLabelTestCases")
    fun `toConsultationPreviewFinished - should return expected`(
        testName: String,
        serverDate: LocalDateTime,
        updateDate: LocalDateTime,
        updateLabel: String?,
        expectedUpdateLabel: String?,
    ) {
        // Given
        mockCurrentDate(serverDate)
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.updateDate).willReturn(updateDate.toDate())
            given(it.updateLabel).willReturn(updateLabel)
        }
        val thematique = mock(Thematique::class.java)

        // When
        val result = mapper.toConsultationPreviewFinished(consultationInfo = consultationInfo, thematique = thematique)

        // Then
        assertThat(result).isEqualTo(
            ConsultationPreviewFinished(
                id = "consultationId",
                title = "title",
                coverUrl = "coverUrl",
                thematique = thematique,
                step = ConsultationStatus.POLITICAL_COMMITMENT,
                updateLabel = expectedUpdateLabel,
            )
        )
    }

    @ParameterizedTest(name = "toConsultationPreviewFinished - {0}")
    @MethodSource("updateLabelTestCases")
    fun `toConsultationPreviewAnswered - should return expected`(
        testName: String,
        serverDate: LocalDateTime,
        updateDate: LocalDateTime,
        updateLabel: String?,
        expectedUpdateLabel: String?,
    ) {
        // Given
        mockCurrentDate(serverDate)
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.updateDate).willReturn(updateDate.toDate())
            given(it.updateLabel).willReturn(updateLabel)
        }
        val thematique = mock(Thematique::class.java)

        // When
        val result = mapper.toConsultationPreviewAnswered(consultationInfo = consultationInfo, thematique = thematique)

        // Then
        assertThat(result).isEqualTo(
            ConsultationPreviewAnswered(
                id = "consultationId",
                title = "title",
                coverUrl = "coverUrl",
                thematique = thematique,
                step = ConsultationStatus.POLITICAL_COMMITMENT,
                updateLabel = expectedUpdateLabel,
            )
        )
    }

    private fun mockCurrentDate(serverDate: LocalDateTime) {
        mapper = ConsultationPreviewFinishedMapper(clock = TestUtils.getFixedClock(serverDate))
    }

}