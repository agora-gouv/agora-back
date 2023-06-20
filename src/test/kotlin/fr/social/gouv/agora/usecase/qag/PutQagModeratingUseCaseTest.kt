package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.QagUpdates
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
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class PutQagModeratingUseCaseTest {

    @Autowired
    private lateinit var useCase: PutQagModeratingUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var qagUpdatesRepository: QagUpdatesRepository

    @MockBean
    private lateinit var sendNotificationQagModeratedUseCase: SendNotificationQagModeratedUseCase

    @Test
    fun `putModeratingQagStatus - when qag is not found - should return FAILURE`() {
        // Given
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.putModeratingQagStatus(qagId = "qagId", qagModeratingStatus = true, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.FAILURE)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `putModeratingQagStatus - when qag status is not OPEN - should return FAILURE`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.ARCHIVED)
        }
        given(qagInfoRepository.getQagInfo("qagId")).willReturn(qagInfo)

        // When
        val result = useCase.putModeratingQagStatus(qagId = "qagId", qagModeratingStatus = true, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.FAILURE)
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is true and updateQagStatus return SUCCESS - should return SUCCESS`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo("qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED))
            .willReturn(QagUpdateResult.SUCCESS)

        // When
        val result = useCase.putModeratingQagStatus(qagId = "qagId", qagModeratingStatus = true, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationQagModeratedUseCase).should(only()).sendNotificationQagAccepted(qagId = "qagId")
        then(qagUpdatesRepository).should(only())
            .insertQagUpdates(
                QagUpdates(
                    qagId = "qagId",
                    newQagStatus = QagStatus.MODERATED_ACCEPTED,
                    userId = "userId"
                )
            )
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is true and updateQagStatus return FAILURE - should return FAILURE`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo("qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED))
            .willReturn(QagUpdateResult.FAILURE)

        // When
        val result = useCase.putModeratingQagStatus(qagId = "qagId", qagModeratingStatus = true, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.FAILURE)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_ACCEPTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is false and updateQagStatus return SUCCESS - should return SUCCESS`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo("qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED))
            .willReturn(QagUpdateResult.SUCCESS)

        // When
        val result = useCase.putModeratingQagStatus(qagId = "qagId", qagModeratingStatus = false, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.SUCCESS)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationQagModeratedUseCase).should(only()).sendNotificationQagRejected(qagId = "qagId")
        then(qagUpdatesRepository).should(only())
            .insertQagUpdates(
                QagUpdates(
                    qagId = "qagId",
                    newQagStatus = QagStatus.MODERATED_REJECTED,
                    userId = "userId"
                )
            )
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is false and updateQagStatus return FAILURE - should return FAILURE`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo("qagId")).willReturn(qagInfo)
        given(qagInfoRepository.updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED))
            .willReturn(QagUpdateResult.FAILURE)

        // When
        val result = useCase.putModeratingQagStatus(qagId = "qagId", qagModeratingStatus = false, userId = "userId")

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.FAILURE)
        then(qagInfoRepository).should().getQagInfo(qagId = "qagId")
        then(qagInfoRepository).should().updateQagStatus(qagId = "qagId", newQagStatus = QagStatus.MODERATED_REJECTED)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }
}