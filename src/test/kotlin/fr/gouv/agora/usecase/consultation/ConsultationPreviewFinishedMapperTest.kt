package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.TestUtils
import fr.gouv.agora.TestUtils.lenientGiven
import fr.gouv.agora.TestUtils.willReturn
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.Month

@ExtendWith(MockitoExtension::class)
class ConsultationPreviewFinishedMapperTest {
    private var mapper: ConsultationPreviewFinishedMapper = ConsultationPreviewFinishedMapper()

    companion object {
        @JvmStatic
        fun updateLabelTestCases() = arrayOf(
            input(
                testName = "when updateLabel is null - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                updateLabel = null,
                expectedUpdateLabel = null,
            ),
            input(
                testName = "when serverDate < updateDate - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                updateLabel = "New update !",
                expectedUpdateLabel = null,
            ),
            input(
                testName = "when updateDate < serverDate < updateDate+90d - should return updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 7, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                updateLabel = "New update !",
                expectedUpdateLabel = "New update !",
            ),
            input(
                testName = "when updateDate < updateDate+90d < serverDate - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.APRIL, 10, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                updateLabel = "New update !",
                expectedUpdateLabel = null,
            ),
            input(
                testName = "when updateDate = serverDate - should return updateLabel",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                updateLabel = "New update !",
                expectedUpdateLabel = "New update !",
            ),
            input(
                testName = "when updateDate+90d = serverDate - should return updateLabel",
                serverDate = LocalDateTime.of(2024, Month.MARCH, 31, 12, 30),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                updateLabel = "New update !",
                expectedUpdateLabel = "New update !",
            ),
            input(
                testName = "when updateDate+90d + 1s = serverDate - should return null updateLabel",
                serverDate = LocalDateTime.of(2024, Month.MARCH, 31, 12, 30, 1),
                updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                endDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
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
            endDate: LocalDateTime,
        ) = arrayOf(testName, serverDate, updateDate, endDate, updateLabel, expectedUpdateLabel)
    }

    @Test
    fun `toConsultationPreviewFinished - when serverDate has not reached endDate - should return status COLLECTING_DATA`() {
        // Given
        val now = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30)
        val updateDate = LocalDateTime.of(2024, 12, 23, 12, 0)
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.endDate).willReturn(LocalDateTime.of(2024, Month.JANUARY, 30, 18, 45))
            given(it.updateLabel).willReturn(null)
            given(it.updateDate).willReturn(updateDate)
        }
        val thematique = mock(Thematique::class.java)

        // When
        val result = mapper.toConsultationPreviewFinished(consultationInfo, thematique)

        // Then
        assertThat(result.getStep(now))
            .isEqualTo(ConsultationStatus.COLLECTING_DATA)
    }

    @Test
    fun `toConsultationPreviewFinished - when serverDate is greater than endDate - should return status POLITICAL_COMMITMENT`() {
        // Given
        val now = LocalDateTime.of(2024, Month.JANUARY, 10, 12, 30)
        val updateDate = LocalDateTime.of(2024, 12, 23, 12, 0)
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.endDate).willReturn(LocalDateTime.of(2024, Month.JANUARY, 1, 4, 15))
            given(it.updateLabel).willReturn(null)
            given(it.updateDate).willReturn(updateDate)
        }
        val thematique = mock(Thematique::class.java)

        // When
        val result = mapper.toConsultationPreviewFinished(consultationInfo, thematique)

        // Then
        assertThat(result.getStep(now))
            .isEqualTo(ConsultationStatus.POLITICAL_COMMITMENT)
    }

    @Test
    fun `toConsultationPreviewFinished - when serverDate is equal to endDate - should return status POLITICAL_COMMITMENT`() {
        // Given
        val now = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30)
        val updateDate = LocalDateTime.of(2024, 12, 23, 12, 0)
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.endDate).willReturn(now)
            given(it.updateLabel).willReturn(null)
            given(it.updateDate).willReturn(updateDate)
        }
        val thematique = mock(Thematique::class.java)

        // When
        val result = mapper.toConsultationPreviewFinished(consultationInfo, thematique)

        // Then
        assertThat(result.getStep(now))
            .isEqualTo(ConsultationStatus.POLITICAL_COMMITMENT)
    }

    @ParameterizedTest(name = "toConsultationPreviewFinished - {0}")
    @MethodSource("updateLabelTestCases")
    fun `toConsultationPreviewFinished - should return expected updateLabel`(
        @Suppress("UNUSED_PARAMETER")
        testName: String,
        serverDate: LocalDateTime,
        updateDate: LocalDateTime,
        endDate: LocalDateTime,
        updateLabel: String?,
        expectedUpdateLabel: String?,
    ) {
        // Given
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.title).willReturn("title")
            given(it.coverUrl).willReturn("coverUrl")
            given(it.endDate).willReturn(LocalDateTime.MIN)
            lenientGiven(it.updateDate).willReturn(updateDate)
            given(it.updateLabel).willReturn(updateLabel)
        }
        val thematique = mock(Thematique::class.java)

        // When
        val result = mapper.toConsultationPreviewFinished(consultationInfo = consultationInfo, thematique = thematique)

        // Then
        assertThat(result.getUpdateLabel(serverDate)).isEqualTo(expectedUpdateLabel)
    }
}
