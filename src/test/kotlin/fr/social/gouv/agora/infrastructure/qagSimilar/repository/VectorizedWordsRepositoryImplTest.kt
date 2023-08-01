package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import fr.social.gouv.agora.infrastructure.qagSimilar.repository.VectorizedWordsCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class VectorizedWordsRepositoryImplTest {

    @Autowired
    private lateinit var repository: VectorizedWordsRepositoryImpl

    @MockBean
    private lateinit var cacheRepository: VectorizedWordsCacheRepository

    @MockBean
    private lateinit var fileRepository: VectorizedWordsFileRepository

    @Test
    fun `getWordVectors - when has all words in cache - should return filtered map from cache`() {
        // Given
        val totoVector = mock(INDArray::class.java)
        val coucouVector = mock(INDArray::class.java)
        given(cacheRepository.getWordVectors()).willReturn(
            CacheResult.CachedWordVectorMap(
                mapOf(
                    "toto" to totoVector,
                    "coucou" to coucouVector,
                )
            )
        )

        // When
        val result = repository.getWordVectors(listOf("toto"))

        // Then
        assertThat(result).isEqualTo(
            mapOf("toto" to totoVector)
        )
        then(cacheRepository).should(only()).getWordVectors()
        then(fileRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getWordVectors - when has missing words in cache - should search in file, insert them in cache then return map`() {
        // Given
        val totoVector = mock(INDArray::class.java)
        given(cacheRepository.getWordVectors()).willReturn(CacheResult.CacheNotInitialized)
        given(fileRepository.getWordVectors(listOf("toto"))).willReturn(mapOf("toto" to totoVector))

        // When
        val result = repository.getWordVectors(listOf("toto"))

        // Then
        assertThat(result).isEqualTo(
            mapOf("toto" to totoVector)
        )
        then(cacheRepository).should().getWordVectors()
        then(cacheRepository).should().addWordVectors(mapOf("toto" to totoVector))
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(fileRepository).should(only()).getWordVectors(listOf("toto"))
    }

    @Test
    fun `getWordVectors - when has missing words in cache & file - should add null to cache then return map`() {
        // Given
        val titiVector = mock(INDArray::class.java)
        val totoVector = mock(INDArray::class.java)
        given(cacheRepository.getWordVectors()).willReturn(CacheResult.CachedWordVectorMap(mapOf("titi" to titiVector)))
        given(fileRepository.getWordVectors(listOf("toto", "tata"))).willReturn(mapOf("toto" to totoVector))

        // When
        val result = repository.getWordVectors(listOf("toto", "titi", "tata"))

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                "toto" to totoVector,
                "titi" to titiVector,
                "tata" to null,
            )
        )
        then(cacheRepository).should().getWordVectors()
        then(cacheRepository).should().addWordVectors(
            mapOf(
                "toto" to totoVector,
                "titi" to titiVector,
                "tata" to null,
            )
        )
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(fileRepository).should(only()).getWordVectors(listOf("toto", "tata"))
    }

}