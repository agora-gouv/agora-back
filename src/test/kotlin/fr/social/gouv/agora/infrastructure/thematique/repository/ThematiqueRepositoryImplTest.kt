package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.social.gouv.agora.infrastructure.thematique.repository.ThematiqueRepositoryImpl.Companion.THEMATIQUE_CACHE_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableCaching
@ImportAutoConfiguration(
    classes = [
        CacheAutoConfiguration::class,
        RedisAutoConfiguration::class,
    ]
)
internal class ThematiqueRepositoryImplTest {

    @Autowired
    private lateinit var repository: ThematiqueRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ThematiqueDatabaseRepository

    @MockBean
    private lateinit var thematiqueMapper: ThematiqueMapper

    @Autowired
    @Suppress("unused")
    private lateinit var cacheManager: CacheManager

    private val thematique = Thematique(
        id = "1337",
        label = "domain-label",
        picto = "domain-picto",
        color = "domain-color",
    )

    private val thematiqueDto = ThematiqueDTO(
        id = UUID.randomUUID(),
        label = "dto-label",
        picto = "dto-picto",
        color = "dto-color",
    )

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(THEMATIQUE_CACHE_NAME)?.clear()
    }

    @Test
    fun `getThematiqueList - when has no cache - should return parsed dto from databaseRepository`() {
        // Given
        given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDto))
        given(thematiqueMapper.toDomain(thematiqueDto)).willReturn(thematique)

        // When
        val result = repository.getThematiqueList()

        // Then
        assertThat(result).isEqualTo(listOf(thematique))
        then(databaseRepository).should(only()).getThematiqueList()
    }

    @Test
    fun `getThematiqueList - when has cache - should return parsed entity from cache`() {
        // Given
        given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDto))
        given(thematiqueMapper.toDomain(thematiqueDto)).willReturn(thematique)

        // When
        repository.getThematiqueList()
        val result = repository.getThematiqueList()

        // Then
        assertThat(result).isEqualTo(listOf(thematique))
        then(databaseRepository).should(times(1)).getThematiqueList()
    }

}