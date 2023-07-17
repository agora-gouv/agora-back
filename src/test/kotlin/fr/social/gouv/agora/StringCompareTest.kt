package fr.social.gouv.agora

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator
import java.io.File

@ExtendWith(SpringExtension::class)
@SpringBootTest
class StringCompareTest {

    @Test
    fun `compareString - when - should `() {
        // Given
        val sentenceIterator = BasicLineIterator(File(this.javaClass.classLoader.getResource("data.txt")!!.file))
        val word2vec = Word2Vec.Builder()
            .minWordFrequency(1)
            .iterations(1)
            .layerSize(100)
            .seed(1337L)
            .windowSize(5)
            .iterate(sentenceIterator)
            .tokenizerFactory(DefaultTokenizerFactory())
            .build()
        word2vec.fit()

        // When
        val result = word2vec.similarity("vidéo", "surveillance")
        println("Result = $result")


        // Then
        assertThat(true).isFalse
    }

}