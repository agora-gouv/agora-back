package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagModerateResult
import fr.social.gouv.agora.domain.QagInsertingUpdates
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ModerateModeratusQagUseCaseTest {

    @Autowired
    private lateinit var useCase: ModerateModeratusQagUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var sendNotificationdUseCase: SendNotificationQagModeratedUseCase

    @MockBean
    private lateinit var qagUpdatesRepository: QagUpdatesRepository

    @Test
    fun `moderateQag - when QaG not found - should return NOT_FOUND`() {
        // Given
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.moderateQag(qagId = "qagId", isAccepted = false, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.NOT_FOUND)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found but status is ARCHIVED - should return NOT_FOUND`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.ARCHIVED)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.moderateQag(qagId = "qagId", isAccepted = false, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.NOT_FOUND)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found but status is SELECTED_FOR_RESPONSE - should return NOT_FOUND`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.SELECTED_FOR_RESPONSE)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.moderateQag(qagId = "qagId", isAccepted = false, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.NOT_FOUND)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is valid but update fails - should return FAILURE`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED))
            .willReturn(QagUpdateResult.FAILURE)

        // When
        val result = useCase.moderateQag(qagId = "qagId", isAccepted = false, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.FAILURE)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `moderateQag - when QaG found, status is valid and isAccepted - should update status to ACCEPTED_MODERATED, send accepted notification, insert QaG update then return result from update`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED))
            .willReturn(QagUpdateResult.SUCCESS)

        // When
        val result = useCase.moderateQag(qagId = "qagId", isAccepted = true, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratusQagModerateResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationdUseCase).should(only()).sendNotificationQagAccepted(qagId = "qagId")
        then(qagUpdatesRepository).should(only()).insertQagUpdates(
            QagInsertingUpdates(
                qagId = "qagId",
                newQagStatus = QagStatus.MODERATED_ACCEPTED,
                userId = "userId",
            )
        )
    }

}