package fr.gouv.agora.usecase.moderatus

import fr.gouv.agora.domain.ModeratusQagModerateResult
import fr.gouv.agora.domain.QagInsertingUpdates
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.gouv.agora.usecase.notification.SendQagNotificationUseCase
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ModerateModeratusQagUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ModerateModeratusQagUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var sendNotificationdUseCase: SendQagNotificationUseCase

    @Mock
    private lateinit var qagUpdatesRepository: QagUpdatesRepository

    @Mock
    private lateinit var moderatusQagLockRepository: ModeratusQagLockRepository

    private val moderateQagOptions = ModerateQagOptions(
        qagId = "qagId",
        userId = "userId",
        isAccepted = true,
        reason = null,
        shouldDelete = false,
    )

    private val expectedInsertingUpdates = QagInsertingUpdates(
        qagId = "qagId",
        newQagStatus = QagStatus.MODERATED_ACCEPTED,
        userId = "userId",
        reason = null,
        shouldDelete = false,
    )

    @Test
    fun `moderateQag - when QaG not found - should return NOT_FOUND`() {
        // Given
        given(qagInfoRepository.getQagInfo(qagId = "notFoundQagId")).willReturn(null)

        // When
        val result = useCase.moderateQag(moderateQagOptions.copy(qagId = "notFoundQagId"))

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.NOT_FOUND)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "notFoundQagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found but status is ARCHIVED - should return NOT_FOUND`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.ARCHIVED)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.moderateQag(moderateQagOptions.copy(qagId = "qagId"))

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.NOT_FOUND)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found but status is SELECTED_FOR_RESPONSE - should return NOT_FOUND`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.SELECTED_FOR_RESPONSE)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.moderateQag(moderateQagOptions.copy(qagId = "qagId"))

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.NOT_FOUND)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is valid but update fails - should return FAILURE`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED))
            .willReturn(QagUpdateResult.Failure)

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = false,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.FAILURE)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is OPEN and isAccepted - should update status to MODERATED_ACCEPTED, send accepted notification, insert QaG update with data from options, remove from locked then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED))
            .willReturn(mock(QagUpdateResult.Success::class.java))

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = true,
                userId = "userId",
                reason = "reason",
                shouldDelete = true,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).should(only()).sendNotificationQagAccepted(qagId = "qagId")
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            expectedInsertingUpdates.copy(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_ACCEPTED,
                userId = "userId",
                reason = "reason",
                shouldDelete = true,
            )
        )
        then(moderatusQagLockRepository).should(only()).removeLockedQagId(qagId = "qagId")
    }

    @Test
    fun `moderateQag - when QaG found, status is OPEN and not isAccepted - should update status to MODERATED_REJECTED, send rejected notification, insert QaG update, remove from locked then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED))
            .willReturn(mock(QagUpdateResult.Success::class.java))

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = false,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).should(only()).sendNotificationQagRejected(qagId = "qagId")
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            expectedInsertingUpdates.copy(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_REJECTED,
            )
        )
        then(moderatusQagLockRepository).should(only()).removeLockedQagId(qagId = "qagId")
    }

    @Test
    fun `moderateQag - when QaG found, status is MODERATED_ACCEPTED and isAccepted - should only insert QaG update then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = true,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            expectedInsertingUpdates.copy(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_ACCEPTED,
            )
        )
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is MODERATED_ACCEPTED and not isAccepted - should update status to MODERATED_REJECTED, send rejected notification, insert QaG update then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED))
            .willReturn(mock(QagUpdateResult.Success::class.java))

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = false,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).should(only()).sendNotificationQagRejected(qagId = "qagId")
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            expectedInsertingUpdates.copy(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_REJECTED,
            )
        )
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is MODERATED_REJECTED and not isAccepted - should only insert QaG update then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_REJECTED)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = false,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            expectedInsertingUpdates.copy(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_REJECTED,
            )
        )
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is MODERATED_REJECTED and isAccepted - should update status to MODERATED_REJECTED, send acceptedAfterReject notification, insert QaG update then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_REJECTED)
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED))
            .willReturn(mock(QagUpdateResult.Success::class.java))

        // When
        val result = useCase.moderateQag(
            moderateQagOptions.copy(
                qagId = "qagId",
                isAccepted = true,
            )
        )

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).should(only()).sendNotificationQagAcceptedAfterReject(qagId = "qagId")
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            expectedInsertingUpdates.copy(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_ACCEPTED,
            )
        )
        then(moderatusQagLockRepository).shouldHaveNoInteractions()
    }

}
