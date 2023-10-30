package fr.social.gouv.agora.infrastructure.qagSimilar.repository

//@ExtendWith(SpringExtension::class)
//@SpringBootTest
//internal class VectorizedWordsRepositoryImplTest {
//
//    @Autowired
//    private lateinit var repository: VectorizedWordsRepositoryImpl
//
//    @MockBean
//    private lateinit var cacheRepository: VectorizedWordsCacheRepository
//
//    @MockBean
//    private lateinit var fileRepository: VectorizedWordsFileRepository
//
//    @Test
//    fun `getWordVectors - when has all words in cache - should return map from cache`() {
//        // Given
//        val totoVector = mock(INDArray::class.java)
//        given(cacheRepository.getWordVectors(listOf("toto"))).willReturn(
//            mapOf("toto" to CacheResult.CachedWordVector(totoVector))
//        )
//
//        // When
//        val result = repository.getWordVectors(listOf("toto"))
//
//        // Then
//        assertThat(result).isEqualTo(
//            mapOf("toto" to totoVector)
//        )
//        then(cacheRepository).should(only()).getWordVectors(listOf("toto"))
//        then(fileRepository).shouldHaveNoInteractions()
//    }
//
//    @Test
//    fun `getWordVectors - when has missing words in cache and found in file - should insert them in cache then return map`() {
//        // Given
//        val totoVector = mock(INDArray::class.java)
//        given(cacheRepository.getWordVectors(listOf("toto"))).willReturn(mapOf("toto" to CacheResult.CacheNotInitialized))
//        given(fileRepository.getWordVectors(listOf("toto")))
//            .willReturn(mapOf("toto" to VectorResult.VectorFound(totoVector)))
//
//        // When
//        val result = repository.getWordVectors(listOf("toto"))
//
//        // Then
//        assertThat(result).isEqualTo(
//            mapOf("toto" to totoVector)
//        )
//        then(cacheRepository).should().getWordVectors(listOf("toto"))
//        then(cacheRepository).should().insertWordVectors(mapOf("toto" to totoVector))
//        then(cacheRepository).shouldHaveNoMoreInteractions()
//        then(fileRepository).should(only()).getWordVectors(listOf("toto"))
//    }
//
//    @Test
//    fun `getWordVectors - when has missing words in cache and error in file - should not insert them in cache then return map`() {
//        // Given
//        given(cacheRepository.getWordVectors(listOf("toto"))).willReturn(mapOf("toto" to CacheResult.CacheNotInitialized))
//        given(fileRepository.getWordVectors(listOf("toto"))).willReturn(mapOf("toto" to VectorResult.VectorError))
//
//        // When
//        val result = repository.getWordVectors(listOf("toto"))
//
//        // Then
//        assertThat(result).isEqualTo(emptyMap<String, INDArray?>())
//        then(cacheRepository).should(only()).getWordVectors(listOf("toto"))
//        then(fileRepository).should(only()).getWordVectors(listOf("toto"))
//    }
//
//    @Test
//    fun `getWordVectors - when has missing words in cache & file - should insert null to cache then return map`() {
//        // Given
//        val totoVector = mock(INDArray::class.java)
//        given(cacheRepository.getWordVectors(listOf("toto", "titi", "tata"))).willReturn(
//            mapOf(
//                "toto" to CacheResult.CachedWordVector(totoVector)
//            )
//        )
//        val titiVector = mock(INDArray::class.java)
//        given(fileRepository.getWordVectors(listOf("titi", "tata")))
//            .willReturn(mapOf("titi" to VectorResult.VectorFound(titiVector)))
//
//        // When
//        val result = repository.getWordVectors(listOf("toto", "titi", "tata"))
//
//        // Then
//        assertThat(result).isEqualTo(
//            mapOf(
//                "toto" to totoVector,
//                "titi" to titiVector,
//                "tata" to null,
//            )
//        )
//        then(cacheRepository).should().getWordVectors(listOf("toto", "titi", "tata"))
//        then(cacheRepository).should().insertWordVectors(
//            mapOf(
//                "titi" to titiVector,
//                "tata" to null,
//            )
//        )
//        then(cacheRepository).shouldHaveNoMoreInteractions()
//        then(fileRepository).should(only()).getWordVectors(listOf("titi", "tata"))
//    }
//
//}