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
    fun `sanitize - when is plain text - should return text`() {
        // When
        val result = contentSanitizer.sanitize("Coucou, ça va ?", 100)

        // Then
        assertThat(result).isEqualTo("Coucou, ça va ?")
    }

    @Test
    fun `sanitize - when has some special characters - should keep special characters`() {
        // When
        val result = contentSanitizer.sanitize("Est-ce que (42 * 5) + 1337 >= 9000 ?!", 100)

        // Then
        assertThat(result).isEqualTo("Est-ce que (42 * 5) + 1337 >= 9000 ?!")
    }

    @Test
    fun `sanitize - when has HTML - should remove HTML elements and replace special character`() {
        // When
        val result = contentSanitizer.sanitize("<a href=\"http://monsupersite.com\">Click ici !</a>", 100)

        // Then
        assertThat(result).isEqualTo("Click ici !")
    }

    @Test
    fun `sanitize - when has more than indicated maxLength - should return trim text to maxLength`() {
        // When
        val result = contentSanitizer.sanitize("1234567890", 3)

        // Then
        assertThat(result).isEqualTo("123")
    }

}