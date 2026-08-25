package fr.gouv.agora.oninit

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.AcmeCertificateRenewalUseCase
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AcmeCertificateRenewalTasksHandlerTest {

    @Mock
    private lateinit var acmeConfig: AcmeConfig

    @Mock
    private lateinit var acmeCertificateRenewalUseCase: AcmeCertificateRenewalUseCase

    @InjectMocks
    private lateinit var handler: AcmeCertificateRenewalTasksHandler

    @Nested
    inner class HandleTask {

        @Test
        fun `handleTask - when ACME_CRON_ENABLED is false - should not call renewIfNeeded`() {
            // Given
            given(acmeConfig.cronEnabled).willReturn(false)

            // When
            handler.handleTask(null)

            // Then
            then(acmeCertificateRenewalUseCase).shouldHaveNoInteractions()
        }

        @Test
        fun `handleTask - when ACME_CRON_ENABLED is true - should call renewIfNeeded`() {
            // Given
            given(acmeConfig.cronEnabled).willReturn(true)

            // When
            handler.handleTask(null)

            // Then
            then(acmeCertificateRenewalUseCase).should().renewIfNeeded()
            then(acmeCertificateRenewalUseCase).shouldHaveNoMoreInteractions()
        }
    }
}
