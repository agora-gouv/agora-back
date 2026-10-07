package fr.gouv.agora.infrastructure.consultationPaginated.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.only
import org.mockito.BDDMockito.then
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisKeyCommands
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.ScanOptions
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class ConsultationsAnsweredPaginatedListCacheRepositoryImplTest {

    private lateinit var cacheManager: CacheManager
    private lateinit var objectMapper: ObjectMapper
    private lateinit var redisConnectionFactory: RedisConnectionFactory
    private lateinit var repository: ConsultationsAnsweredPaginatedListCacheRepositoryImpl

    @BeforeEach
    fun setUp() {
        cacheManager = ConcurrentMapCacheManager()
        objectMapper = jacksonObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
        redisConnectionFactory = mock(RedisConnectionFactory::class.java)
        repository = ConsultationsAnsweredPaginatedListCacheRepositoryImpl(
            cacheManager = cacheManager,
            objectMapper = objectMapper,
            redisConnectionFactory = redisConnectionFactory,
        )
    }

    @Nested
    inner class `get and init` {

        @Test
        fun `when no content cached - should return null`() {
            assertThat(repository.getConsultationAnsweredPage(userId = "userA", pageNumber = 1)).isNull()
        }

        @Test
        fun `when content cached for a user and page - should return the exact same content`() {
            val content = buildPage(consultationId = "consultation-A", maxPageNumber = 2)

            repository.initConsultationAnsweredPage(userId = "userA", pageNumber = 1, content = content)

            assertThat(repository.getConsultationAnsweredPage(userId = "userA", pageNumber = 1)).isEqualTo(content)
        }

        @Test
        fun `when two users cache the same page - content should be isolated per user`() {
            val userAContent = buildPage(consultationId = "consultation-A", maxPageNumber = 2)
            val userBContent = buildPage(consultationId = "consultation-B", maxPageNumber = 5)

            repository.initConsultationAnsweredPage(userId = "userA", pageNumber = 1, content = userAContent)
            repository.initConsultationAnsweredPage(userId = "userB", pageNumber = 1, content = userBContent)

            assertThat(repository.getConsultationAnsweredPage(userId = "userA", pageNumber = 1)).isEqualTo(userAContent)
            assertThat(repository.getConsultationAnsweredPage(userId = "userB", pageNumber = 1)).isEqualTo(userBContent)
        }

        @Test
        fun `when same user caches two pages - content should be isolated per page`() {
            val page1 = buildPage(consultationId = "consultation-page-1", maxPageNumber = 3)
            val page2 = buildPage(consultationId = "consultation-page-2", maxPageNumber = 3)

            repository.initConsultationAnsweredPage(userId = "userA", pageNumber = 1, content = page1)
            repository.initConsultationAnsweredPage(userId = "userA", pageNumber = 2, content = page2)

            assertThat(repository.getConsultationAnsweredPage(userId = "userA", pageNumber = 1)).isEqualTo(page1)
            assertThat(repository.getConsultationAnsweredPage(userId = "userA", pageNumber = 2)).isEqualTo(page2)
        }

        @Test
        fun `when caching - should use a single shared cache name instead of a per-user cache name`() {
            repository.initConsultationAnsweredPage(userId = "userA", pageNumber = 1, content = buildPage("c-A", 1))
            repository.initConsultationAnsweredPage(userId = "userB", pageNumber = 1, content = buildPage("c-B", 1))

            assertThat(cacheManager.cacheNames).containsExactly("consultationsAnsweredPaginated")
        }
    }

    @Nested
    inner class clearCache {

        @Test
        fun `clearCache - should scan and delete only the keys of the given user`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            @Suppress("UNCHECKED_CAST")
            val cursor = mock(Cursor::class.java) as Cursor<ByteArray>
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(keyCommands.scan(any(ScanOptions::class.java))).willReturn(cursor)
            given(cursor.hasNext()).willReturn(true, false)
            given(cursor.next()).willReturn("consultationsAnsweredPaginated::userA/1".toByteArray())

            // When
            repository.clearCache(userId = "userA")

            // Then
            val scanOptionsCaptor = ArgumentCaptor.forClass(ScanOptions::class.java)
            then(keyCommands).should().scan(scanOptionsCaptor.capture())
            assertThat(scanOptionsCaptor.value.pattern).isEqualTo("consultationsAnsweredPaginated::userA/*")
            then(keyCommands).should().del("consultationsAnsweredPaginated::userA/1".toByteArray())
            then(connection).should().close()
        }

        @Test
        fun `clearCache - when no key matches the user - should not delete anything`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            @Suppress("UNCHECKED_CAST")
            val cursor = mock(Cursor::class.java) as Cursor<ByteArray>
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(keyCommands.scan(any(ScanOptions::class.java))).willReturn(cursor)
            given(cursor.hasNext()).willReturn(false)

            // When
            repository.clearCache(userId = "userA")

            // Then
            then(keyCommands).should(only()).scan(any(ScanOptions::class.java))
            then(connection).should().close()
        }
    }

    private fun buildPage(consultationId: String, maxPageNumber: Int) = ConsultationAnsweredPaginatedList(
        consultationAnsweredList = listOf(
            ConsultationPreviewFinished(
                id = consultationId,
                slug = "slug-$consultationId",
                title = "title-$consultationId",
                coverUrl = "https://example.com/$consultationId.png",
                thematique = Thematique(id = "thematique-id", label = "thematique-label", picto = "picto"),
                updateLabel = null,
                lastUpdateDate = LocalDateTime.of(2026, 1, 1, 0, 0),
                endDate = LocalDateTime.of(2026, 2, 1, 0, 0),
                territory = "all",
            )
        ),
        maxPageNumber = maxPageNumber,
    )
}
