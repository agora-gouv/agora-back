package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.infrastructure.notification.dto.NotificationDTO
import fr.gouv.agora.infrastructure.notification.repository.NotificationCacheRepository.CacheResult
import fr.gouv.agora.usecase.notification.repository.NotificationInsertionResult
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
internal class NotificationRepositoryImplTest {

    @Autowired
    private lateinit var repository: NotificationRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: NotificationDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: NotificationCacheRepository

    @MockBean
    private lateinit var mapper: NotificationMapper

    @Nested
    inner class InsertNotificationTestCases {
        @Test
        fun `insertNotification - when mapper returns emptyList - should return FAILURE`() {
            // Given
            val notification = mock(NotificationInserting::class.java)
            given(mapper.toDto(notification)).willReturn(emptyList())

            // When
            val result = repository.insertNotifications(notification)

            // Then
            assertThat(result).isEqualTo(NotificationInsertionResult.FAILURE)
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertNotification - when mapper returns DTO - should return SUCCESS`() {
            // Given
            val notification = mock(NotificationInserting::class.java)
            val notificationDTO = mock(NotificationDTO::class.java)
            given(mapper.toDto(notification)).willReturn(listOf(notificationDTO))

            val savedNotificationDTO = mock(NotificationDTO::class.java)
            given(databaseRepository.saveAll(listOf(notificationDTO))).willReturn(listOf(savedNotificationDTO))

            // When
            val result = repository.insertNotifications(notification)

            // Then
            assertThat(result).isEqualTo(NotificationInsertionResult.SUCCESS)
            then(databaseRepository).should(only()).saveAll(listOf(notificationDTO))
            then(cacheRepository).should(only()).insertNotification(notificationDTOList = listOf(savedNotificationDTO))
        }

    }

    @Nested
    inner class GetUserNotificationListTestCases {
        @Test
        fun `getUserNotificationList - when invalid UUID - should return emptyList`() {
            // When
            val result = repository.getUserNotificationList(userId = "invalid UUID")

            // Then
            assertThat(result).isEqualTo(emptyList<Notification>())
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserNotificationList - when cache is not initialized and has no result - should initialize cache with database then return emptyList`() {
            // Given
            given(cacheRepository.getAllNotificationList()).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.findAll()).willReturn(emptyList())

            // When
            val result = repository.getUserNotificationList(userId = UUID.randomUUID().toString())

            // Then
            assertThat(result).isEqualTo(emptyList<Notification>())
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getAllNotificationList()
                then(databaseRepository).should(it).findAll()
                then(cacheRepository).should(it).initializeCache(emptyList())
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserNotificationList - when cache is initialized and has result - should return mapped result`() {
            // Given
            val notificationId = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val notificationDTO = mock(NotificationDTO::class.java).also {
                given(it.id).willReturn(notificationId)
                given(it.userId).willReturn(userId)
            }
            // val allNotificationDTO = listOf(notificationDTO)
            given(cacheRepository.getAllNotificationList()).willReturn(
                CacheResult.CachedNotificationList(
                    listOf(
                        notificationDTO
                    )
                )
            )

            val notification = mock(Notification::class.java)
            given(mapper.toDomain(notificationDTO)).willReturn(notification)

            // When
            val result = repository.getUserNotificationList(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(listOf(notification))
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getAllNotificationList()
                then(mapper).should(it).toDomain(notificationDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }
    }
}