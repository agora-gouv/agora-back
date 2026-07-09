package fr.gouv.agora.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiRequestBuilder
import fr.gouv.agora.infrastructure.common.StrapiSingleTypeDTO
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.net.http.HttpClient
import java.net.http.HttpResponse

@ExtendWith(MockitoExtension::class)
internal class CmsStrapiHttpClientTest {

    @Mock
    private lateinit var httpClient: HttpClient

    @Mock
    private lateinit var objectMapper: ObjectMapper

    private fun buildClient(suspended: Boolean) = CmsStrapiHttpClient(
        httpClient = httpClient,
        objectMapper = objectMapper,
        strapiSuspended = suspended,
        cmsAuthToken = "fake-token",
        cmsApiUrl = "http://localhost:1337/api/",
    )

    @Nested
    inner class Request {

        @Test
        fun `request - when STRAPI_SUSPENDED is true - should return empty StrapiDTO without calling HttpClient`() {
            // Given
            val client = buildClient(suspended = true)
            val typeReference = object : TypeReference<StrapiDTO<String>>() {}
            val builder = mock(StrapiRequestBuilder::class.java)

            // When
            val result: StrapiDTO<String> = client.request(builder, typeReference)

            // Then
            assertThat(result.data).isEmpty()
            then(httpClient).shouldHaveNoInteractions()
            then(builder).shouldHaveNoInteractions()
        }

        @Test
        fun `request - when STRAPI_SUSPENDED is false - should call HttpClient and return result`() {
            // Given
            val client = buildClient(suspended = false)
            val builder = buildFakeStrapiRequestBuilder()
            val typeReference = object : TypeReference<StrapiDTO<String>>() {}
            val fakeStrapiDTO: StrapiDTO<String> = StrapiDTO.ofEmpty()

            val fakeHttpResponse = mockHttpResponse("{}")
            given(httpClient.send(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any<HttpResponse.BodyHandler<String>>()))
                .willReturn(fakeHttpResponse)
            given(objectMapper.readValue("{}", typeReference)).willReturn(fakeStrapiDTO)

            // When
            val result: StrapiDTO<String> = client.request(builder, typeReference)

            // Then
            assertThat(result).isEqualTo(fakeStrapiDTO)
        }
    }

    @Nested
    inner class RequestSingleType {

        @Test
        fun `requestSingleType - when STRAPI_SUSPENDED is true - should throw StrapiTrafficSuspendedException without calling HttpClient`() {
            // Given
            val client = buildClient(suspended = true)
            val typeReference = object : TypeReference<StrapiSingleTypeDTO<String>>() {}
            val builder = mock(StrapiRequestBuilder::class.java)

            // When / Then
            assertThatThrownBy {
                client.requestSingleType<String>(builder, typeReference)
            }.isInstanceOf(StrapiTrafficSuspendedException::class.java)

            then(httpClient).shouldHaveNoInteractions()
            then(builder).shouldHaveNoInteractions()
        }

        @Test
        fun `requestSingleType - when STRAPI_SUSPENDED is false - should call HttpClient and return result`() {
            // Given
            val client = buildClient(suspended = false)
            val builder = buildFakeStrapiRequestBuilder()
            val typeReference = object : TypeReference<StrapiSingleTypeDTO<String>>() {}
            val fakeSingleTypeDTO = StrapiSingleTypeDTO("content")

            val fakeHttpResponse = mockHttpResponse("{}")
            given(httpClient.send(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any<HttpResponse.BodyHandler<String>>()))
                .willReturn(fakeHttpResponse)
            given(objectMapper.readValue("{}", typeReference)).willReturn(fakeSingleTypeDTO)

            // When
            val result: StrapiSingleTypeDTO<String> = client.requestSingleType(builder, typeReference)

            // Then
            assertThat(result).isEqualTo(fakeSingleTypeDTO)
        }
    }

    private fun buildFakeStrapiRequestBuilder(): StrapiRequestBuilder {
        val builder = mock(StrapiRequestBuilder::class.java)
        given(builder.build()).willReturn("content-types")
        return builder
    }

    @Suppress("UNCHECKED_CAST")
    private fun mockHttpResponse(body: String): HttpResponse<String> {
        val response = mock(HttpResponse::class.java) as HttpResponse<String>
        given(response.body()).willReturn(body)
        return response
    }
}
