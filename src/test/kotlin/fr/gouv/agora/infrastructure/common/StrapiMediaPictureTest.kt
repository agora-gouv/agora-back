package fr.gouv.agora.infrastructure.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class StrapiMediaPictureTest {

    private val objectMapper = jacksonObjectMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Nested
    inner class `mediaUrl` {

        @Test
        fun `mediaUrl - when formats has medium - should return medium url`() {
            // Given
            val picture = StrapiMediaPicture(
                formats = StrapiMediaPictureFormats(
                    medium = StrapiMediaPictureFormatMedium(url = "https://example.com/medium.jpg")
                ),
                pictureUrlNotOptimized = "https://example.com/original.jpg"
            )

            // When
            val result = picture.mediaUrl()

            // Then
            assertThat(result).isEqualTo("https://example.com/medium.jpg")
        }

        @Test
        fun `mediaUrl - when formats has no medium - should return raw url`() {
            // Given
            val picture = StrapiMediaPicture(
                formats = StrapiMediaPictureFormats(medium = null),
                pictureUrlNotOptimized = "https://example.com/original.jpg"
            )

            // When
            val result = picture.mediaUrl()

            // Then
            assertThat(result).isEqualTo("https://example.com/original.jpg")
        }

        @Test
        fun `mediaUrl - when formats is null - should return raw url`() {
            // Given
            val picture = StrapiMediaPicture(
                formats = null,
                pictureUrlNotOptimized = "https://example.com/original.jpg"
            )

            // When
            val result = picture.mediaUrl()

            // Then
            assertThat(result).isEqualTo("https://example.com/original.jpg")
        }
    }

    @Nested
    inner class `désérialisation JSON` {

        @Test
        fun `parse - when formats present with medium - should deserialize correctly`() {
            // Given
            @Language("JSON")
            val json = """
                {
                  "formats": {
                    "medium": { "url": "https://example.com/medium.jpg" }
                  },
                  "url": "https://example.com/original.jpg"
                }
            """.trimIndent()

            // When
            val result = objectMapper.readValue(json, StrapiMediaPicture::class.java)

            // Then
            assertThat(result.mediaUrl()).isEqualTo("https://example.com/medium.jpg")
            assertThat(result.pictureUrlNotOptimized).isEqualTo("https://example.com/original.jpg")
        }

        @Test
        fun `parse - when formats absent - should deserialize with null formats and use raw url`() {
            // Given
            @Language("JSON")
            val json = """
                {
                  "url": "https://example.com/original.jpg"
                }
            """.trimIndent()

            // When
            val result = objectMapper.readValue(json, StrapiMediaPicture::class.java)

            // Then
            assertThat(result.formats).isNull()
            assertThat(result.mediaUrl()).isEqualTo("https://example.com/original.jpg")
        }

        @Test
        fun `parse - when formats is null - should deserialize with null formats and use raw url`() {
            // Given
            @Language("JSON")
            val json = """
                {
                  "formats": null,
                  "url": "https://example.com/original.jpg"
                }
            """.trimIndent()

            // When
            val result = objectMapper.readValue(json, StrapiMediaPicture::class.java)

            // Then
            assertThat(result.formats).isNull()
            assertThat(result.mediaUrl()).isEqualTo("https://example.com/original.jpg")
        }

        @Test
        fun `parse - when formats has no medium - should use raw url`() {
            // Given
            @Language("JSON")
            val json = """
                {
                  "formats": {},
                  "url": "https://example.com/original.jpg"
                }
            """.trimIndent()

            // When
            val result = objectMapper.readValue(json, StrapiMediaPicture::class.java)

            // Then
            assertThat(result.formats?.medium).isNull()
            assertThat(result.mediaUrl()).isEqualTo("https://example.com/original.jpg")
        }
    }
}
