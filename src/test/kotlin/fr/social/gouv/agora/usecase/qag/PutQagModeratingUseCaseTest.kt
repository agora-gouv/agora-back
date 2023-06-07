package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.notification.SendNotificationQagModeratedUseCase
import fr.social.gouv.agora.usecase.qag.repository.ModeratingQagResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
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
    private lateinit var sendNotificationQagModeratedUseCase: SendNotificationQagModeratedUseCase

    private val qagId = "qagId"

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is true and updateQagStatus return SUCCESS - should return SUCCESS`() {
        // Given
        given(
            qagInfoRepository.updateQagStatus(
                qagId,
                QagStatus.MODERATED_ACCEPTED
            )
        ).willReturn(ModeratingQagResult.SUCCESS)

        // When
        val result = useCase.putModeratingQagStatus(qagId = qagId, qagModeratingStatus = true)

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.SUCCESS)
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is true and updateQagStatus return FAILURE - should return FAILURE`() {
        // Given
        given(
            qagInfoRepository.updateQagStatus(
                qagId,
                QagStatus.MODERATED_ACCEPTED
            )
        ).willReturn(ModeratingQagResult.FAILURE)

        // When
        val result = useCase.putModeratingQagStatus(qagId = qagId, qagModeratingStatus = true)

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.FAILURE)
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is false and updateQagStatus return SUCCESS - should return SUCCESS`() {
        // Given
        given(
            qagInfoRepository.updateQagStatus(
                qagId,
                QagStatus.MODERATED_REJECTED
            )
        ).willReturn(ModeratingQagResult.SUCCESS)

        // When
        val result = useCase.putModeratingQagStatus(qagId = qagId, qagModeratingStatus = false)

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.SUCCESS)
        then(sendNotificationQagModeratedUseCase).should(only()).sendNotificationQagModeratedMessage(qagId = qagId)
    }

    @Test
    fun `putModeratingQagStatus - when qagModeratingStatus is false and updateQagStatus return FAILURE - should return FAILURE`() {
        // Given
        given(
            qagInfoRepository.updateQagStatus(
                qagId,
                QagStatus.MODERATED_REJECTED
            )
        ).willReturn(ModeratingQagResult.FAILURE)

        // When
        val result = useCase.putModeratingQagStatus(qagId = qagId, qagModeratingStatus = false)

        // Then
        assertThat(result).isEqualTo(ModeratingQagResult.FAILURE)
        then(sendNotificationQagModeratedUseCase).shouldHaveNoInteractions()
    }
}