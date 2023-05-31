package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetSupportQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetSupportQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: SupportQagDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: SupportQagCacheRepository

    @MockBean
    private lateinit var mapper: SupportQagMapper

    @Nested
    inner class GetAllSupportQagTestCases {
        @Test
        fun `getAllSupportQag - when cache is not initialized - should initialize cache with database then return mapped results`() {
            // Given
            given(cacheRepository.isInitialized()).willReturn(false)
            val supportQagDTO = mock(SupportQagDTO::class.java)
            given(databaseRepository.getAllSupportQagList()).willReturn(listOf(supportQagDTO))

            val supportQagInfo = mock(SupportQagInfo::class.java)
            given(mapper.toSupportQagInfo(supportQagDTO)).willReturn(supportQagInfo)

            // When
            val result = repository.getAllSupportQag()

            // Then
            assertThat(result).isEqualTo(listOf(supportQagInfo))
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).isInitialized()
                then(databaseRepository).should(it).getAllSupportQagList()
                then(cacheRepository).should(it).initializeCache(listOf(supportQagDTO))
                then(mapper).should(it).toSupportQagInfo(supportQagDTO)
                it.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `getAllSupportQag - when cache is initialized - should then return mapped results`() {
            // Given
            given(cacheRepository.isInitialized()).willReturn(true)
            val supportQagDTO = mock(SupportQagDTO::class.java)
            given(cacheRepository.getAllSupportQagList()).willReturn(listOf(supportQagDTO))

            val supportQagInfo = mock(SupportQagInfo::class.java)
            given(mapper.toSupportQagInfo(supportQagDTO)).willReturn(supportQagInfo)

            // When
            val result = repository.getAllSupportQag()

            // Then
            assertThat(result).isEqualTo(listOf(supportQagInfo))
            inOrder(cacheRepository, mapper).also {
                then(cacheRepository).should(it).isInitialized()
                then(cacheRepository).should(it).getAllSupportQagList()
                then(mapper).should(it).toSupportQagInfo(supportQagDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class GetSupportQagTestCases {
        @Test
        fun `getSupportQag - when invalid qagId - should return null`() {
            // When
            val result = repository.getSupportQag(
                qagId = "invalid qagId UUID",
                userId = UUID.randomUUID().toString(),
            )

            // Then
            assertThat(result).isNull()
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getSupportQag - when invalid userId - should return null`() {
            // When
            val result = repository.getSupportQag(
                qagId = UUID.randomUUID().toString(),
                userId = "invalid userId UUID",
            )

            // Then
            assertThat(result).isNull()
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getSupportQag - when cache is not initialized - should initialize cache with database then return calculated and mapped result`() {
            // Given
            given(cacheRepository.isInitialized()).willReturn(false)

            val qagUUID = UUID.randomUUID()
            val userUUID = UUID.randomUUID()
            val supportQagDTO = createMockSupportQagDTO(qagId = qagUUID, userId = userUUID)
            val allSupportQagDTO = listOf(
                supportQagDTO,
                createMockSupportQagDTO(qagId = qagUUID, userId = UUID.randomUUID()),
                createMockSupportQagDTO(qagId = UUID.randomUUID(), userId = UUID.randomUUID()),
            )
            given(databaseRepository.getAllSupportQagList()).willReturn(allSupportQagDTO)

            val supportQag = mock(SupportQag::class.java)
            given(mapper.toDomain(supportCount = 2, dto = supportQagDTO)).willReturn(supportQag)

            // When
            val result = repository.getSupportQag(
                qagId = qagUUID.toString(),
                userId = userUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(supportQag)
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).isInitialized()
                then(databaseRepository).should(it).getAllSupportQagList()
                then(cacheRepository).should(it).initializeCache(allSupportQagDTO)
                then(mapper).should(it).toDomain(supportCount = 2, dto = supportQagDTO)
                it.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `getSupportQag - when cache is initialized - should return calculated and mapped result`() {
            // Given
            given(cacheRepository.isInitialized()).willReturn(true)
            val qagUUID = UUID.randomUUID()
            val userUUID = UUID.randomUUID()
            val allSupportQagDTO = listOf(
                createMockSupportQagDTO(qagId = qagUUID, userId = UUID.randomUUID()),
                createMockSupportQagDTO(qagId = UUID.randomUUID(), userId = UUID.randomUUID()),
            )
            given(cacheRepository.getAllSupportQagList()).willReturn(allSupportQagDTO)

            val supportQag = mock(SupportQag::class.java)
            given(mapper.toDomain(supportCount = 1, dto = null)).willReturn(supportQag)

            // When
            val result = repository.getSupportQag(
                qagId = qagUUID.toString(),
                userId = userUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(supportQag)
            inOrder(cacheRepository, mapper).also {
                then(cacheRepository).should(it).isInitialized()
                then(cacheRepository).should(it).getAllSupportQagList()
                then(mapper).should(it).toDomain(supportCount = 1, dto = null)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }
    }

    private fun createMockSupportQagDTO(qagId: UUID, userId: UUID) = mock(SupportQagDTO::class.java).also {
        given(it.qagId).willReturn(qagId)
        given(it.userId).willReturn(userId)
    }

}