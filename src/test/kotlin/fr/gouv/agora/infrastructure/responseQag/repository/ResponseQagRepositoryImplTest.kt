package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ResponseQagRepositoryImplTest {
    @InjectMocks
    private lateinit var responseQagRepositoryImpl: ResponseQagRepositoryImpl

    @Mock
    private lateinit var responseQagStrapiRepository: ResponseQagStrapiRepository

    @Mock
    private lateinit var responseQagMapper: ResponseQagMapper

    @Test
    fun `when there is more responses than the from, returns responses`() {
        // Given
        val strapiResponse = mock(StrapiDTO::class.java) as StrapiDTO<StrapiResponseQag>
        val responseQag = mock(ResponseQagText::class.java)

        val from = 2
        val threeResponses = listOf(responseQag, responseQag, responseQag)

        given(responseQagMapper.toDomain(strapiResponse)).willReturn(threeResponses)
        given(responseQagStrapiRepository.getResponsesQag()).willReturn(strapiResponse)

        // When
        val actual = responseQagRepositoryImpl.getResponsesQag(from)

        // Then
        assertThat(actual).hasSize(1)
    }

    @Test
    fun `when the from and number of responses are equals, returns emptyList`() {
        // Given
        val from = 2
        val strapiResponse = mock(StrapiDTO::class.java) as StrapiDTO<StrapiResponseQag>
        val responseQag = mock(ResponseQagText::class.java)

        given(responseQagMapper.toDomain(strapiResponse)).willReturn(listOf(responseQag))
        given(responseQagStrapiRepository.getResponsesQag()).willReturn(strapiResponse)

        // When
        val actual = responseQagRepositoryImpl.getResponsesQag(from)

        // Then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `when the from is superior to number of responses, returns emptyList`() {
        // Given
        val from = 20
        val strapiResponse = mock(StrapiDTO::class.java) as StrapiDTO<StrapiResponseQag>
        val responseQag = mock(ResponseQagText::class.java)

        given(responseQagMapper.toDomain(strapiResponse)).willReturn(listOf(responseQag))
        given(responseQagStrapiRepository.getResponsesQag()).willReturn(strapiResponse)

        // When
        val actual = responseQagRepositoryImpl.getResponsesQag(from)

        // Then
        assertThat(actual).isEmpty()
    }
}
