package fr.social.gouv.agora.usecase.qag

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ContentSanitizerTest {

    @Autowired
    private lateinit var contentSanitizer: ContentSanitizer

    @Test
    fun `isSanitized - when is plain text - should return text`() {
        // When
        val result = contentSanitizer.sanitize("Coucou, ça va ?", 100)

        // Then
        assertThat(result).isEqualTo("Coucou, ça va ?")
    }

    @Test
    fun `isSanitized - when has some special characters - should keep special characters`() {
        // When
        val result = contentSanitizer.sanitize("Est-ce que (42 * 5) + 1337 >= 9000 ?!", 100)

        // Then
        assertThat(result).isEqualTo("Est-ce que (42 * 5) + 1337 >= 9000 ?!")
    }

    @Test
    fun `isSanitized - when has HTML - should remove HTML elements and replace special character`() {
        // When
        val result = contentSanitizer.sanitize("<a href=\"http://monsupersite.com\">Click ici !</a>", 100)

        // Then
        assertThat(result).isEqualTo("Click ici !")
    }

    @Test
    fun `isSanitized - when has more than indicated maxLength - should return trim text to maxLength`() {
        // When
        val result = contentSanitizer.sanitize("1234567890", 3)

        // Then
        assertThat(result).isEqualTo("123")
    }

    @Test
    fun `isContentSaint - when has only plain text - should return true`() {
        // When
        val result = contentSanitizer.isContentSaint("Coucou, ça va ?", 100)

        // Then
        assertThat(result).isTrue
    }

    @Test
    fun `isContentSaint - when has some special characters - should return true`() {
        // When
        val result = contentSanitizer.isContentSaint("Est-ce que (42 * 5) + 1337 >= 9000 ?!", 100)

        // Then
        assertThat(result).isTrue
    }

    @Test
    fun `isContentSaint - when has HTML - should return false`() {
        // When
        val result = contentSanitizer.isContentSaint("<a href=\"http://monsupersite.com\">Click ici !</a>", 100)

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun `isContentSaint - when has more than indicated maxLength - should return false`() {
        // When
        val result = contentSanitizer.isContentSaint("1234567890", 3)

        // Then
        assertThat(result).isFalse
    }
}