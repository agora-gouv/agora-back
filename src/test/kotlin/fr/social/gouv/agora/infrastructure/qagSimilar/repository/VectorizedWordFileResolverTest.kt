package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class VectorizedWordFileResolverTest {

    companion object {
        @JvmStatic
        fun findWordArchiveFileTestCases() = arrayOf(
            input(word = "toto", expectedArchiveFilePointer = "t"),
            input(word = "àlain_terrieur", expectedArchiveFilePointer = "specials"),
            input(word = "æternuer", expectedArchiveFilePointer = "specials"),
            input(word = "øméga3", expectedArchiveFilePointer = "specials"),
            input(word = "2", expectedArchiveFilePointer = "2"),
            input(word = "¾journée", expectedArchiveFilePointer = "specials"),
            input(word = "³mousquetaires", expectedArchiveFilePointer = "specials"),
            input(word = "/!\\ ", expectedArchiveFilePointer = "specials"),
        )

        private fun input(
            word: String,
            expectedArchiveFilePointer: String,
        ) = arrayOf(word, "wordVectors_$expectedArchiveFilePointer")
    }

    @Autowired
    private lateinit var resolver: VectorizedWordFileResolver

    @MethodSource("findWordArchiveFileTestCases")
    @ParameterizedTest(name = "findWordArchiveName - when word is {0} - should return {1}")
    fun `findWordArchiveName - should return expected archive file name`(
        word: String,
        expectedArchiveFileName: String,
    ) {
        // When
        val result = resolver.findWordArchiveName(word)

        // Then
        assertThat(result).isEqualTo(expectedArchiveFileName)
    }

}