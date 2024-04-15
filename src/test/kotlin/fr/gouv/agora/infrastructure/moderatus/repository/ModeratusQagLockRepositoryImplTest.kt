package fr.gouv.agora.infrastructure.moderatus.repository

import fr.gouv.agora.infrastructure.moderatus.dto.ModeratusQagLockDTO
import fr.gouv.agora.infrastructure.moderatus.repository.ModeratusQagLockCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ModeratusQagLockRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: ModeratusQagLockRepositoryImpl

    @Mock
    private lateinit var cacheRepository: ModeratusQagLockCacheRepository

    @Mock
    private lateinit var databaseRepository: ModeratusQagLockDatabaseRepository

    @Mock
    private lateinit var mapper: ModeratusQagLockMapper

    @Test
    fun `getLockedQagIds - when cache initialized - should return cached result`() {
        // Given
        val lockedQagId = "lockedQagId"
        given(cacheRepository.getLockedQagIds()).willReturn(CacheResult.CachedLockedQagIds(listOf(lockedQagId)))

        // When
        val result = repository.getLockedQagIds()

        // Then
        assertThat(result).isEqualTo(listOf(lockedQagId))
        then(cacheRepository).should(only()).getLockedQagIds()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getLockedQagIds - when cache not initialized - should get from database, insert cache then return mapped result`() {
        // Given
        given(cacheRepository.getLockedQagIds()).willReturn(CacheResult.CacheNotInitialized)

        val moderatusQagLockDTO = mock(ModeratusQagLockDTO::class.java)
        given(databaseRepository.findAll()).willReturn(listOf(moderatusQagLockDTO))

        val lockedQagId = "lockedQagId"
        given(mapper.toQagId(moderatusQagLockDTO)).willReturn(lockedQagId)

        // When
        val result = repository.getLockedQagIds()

        // Then
        assertThat(result).isEqualTo(listOf(lockedQagId))
        then(cacheRepository).should().getLockedQagIds()
        then(cacheRepository).should().addLockedQagIds(listOf(lockedQagId))
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).findAll()
        then(mapper).should(only()).toQagId(moderatusQagLockDTO)
    }

    @Test
    fun `addLockedIds - when mapper returns dto - should add to cache and database`() {
        // Given
        val lockedQagId = "lockedQagId"
        val moderatusQagLockDTO = mock(ModeratusQagLockDTO::class.java)
        given(mapper.toDto(lockedQagId)).willReturn(moderatusQagLockDTO)

        val savedModeratusQagLockDTO = mock(ModeratusQagLockDTO::class.java)
        given(databaseRepository.saveAll(listOf(moderatusQagLockDTO))).willReturn(listOf(savedModeratusQagLockDTO))
        val savedLockedQagId = "savedLockedQagId"
        given(mapper.toQagId(savedModeratusQagLockDTO)).willReturn(savedLockedQagId)

        // When
        repository.addLockedIds(listOf(lockedQagId))

        // Then
        then(cacheRepository).should(only()).addLockedQagIds(listOf(savedLockedQagId))
        then(databaseRepository).should(only()).saveAll(listOf(moderatusQagLockDTO))
        then(mapper).should().toDto(lockedQagId)
        then(mapper).should().toQagId(savedModeratusQagLockDTO)
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `addLockedIds - when mapper does not return dto - should do nothing`() {
        // Given
        val lockedQagId = "lockedQagId"
        given(mapper.toDto(lockedQagId)).willReturn(null)

        // When
        repository.addLockedIds(listOf(lockedQagId))

        // Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDto(lockedQagId)
    }

    @Test
    fun `removeLockedQagId - invalid UUID - should do nothing`() {
        // Given
        val lockedQagId = "lockedQagId with invalid UUID"

        // When
        repository.removeLockedQagId(lockedQagId)

        // Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `removeLockedQagId - when valid UUID - should remove from database and cache`() {
        // Given
        val lockedQagId = UUID.randomUUID()

        // When
        repository.removeLockedQagId(lockedQagId.toString())

        // Then
        then(cacheRepository).should(only()).removeLockedQagId(lockedQagId.toString())
        then(databaseRepository).should(only()).deleteLockedQag(lockedQagId)
        then(mapper).shouldHaveNoInteractions()
    }

}