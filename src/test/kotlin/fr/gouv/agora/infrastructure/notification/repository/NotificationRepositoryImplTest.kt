package fr.gouv.agora.infrastructure.notification.repository

import fr.gouv.agora.domain.Notification
import fr.gouv.agora.domain.NotificationInserting
import fr.gouv.agora.infrastructure.notification.dto.NotificationDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.notification.repository.NotificationInsertionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class NotificationRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: NotificationRepositoryImpl

    @Mock
    private lateinit var databaseRepository: NotificationDatabaseRepository

    @Mock
    private lateinit var cacheRepository: NotificationCacheRepository

    @Mock
    private lateinit var mapper: NotificationMapper

    @Nested
    inner class InsertNotificationTestCases {
        @Test
        fun `insertNotification - when mapper returns emptyList`() {
            // Given
            val notification = mock(NotificationInserting::class.java)
            given(mapper.toDto(notification)).willReturn(emptyList())

            // When
            repository.insertNotifications(notification)

            // Then
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertNotification - when mapper returns DTO - should insert notifications`() {
            // Given
            val notification = mock(NotificationInserting::class.java)
            val notificationDTO = mock(NotificationDTO::class.java)
            given(mapper.toDto(notification)).willReturn(listOf(notificationDTO))

            val savedNotificationDTO = mock(NotificationDTO::class.java)
            given(databaseRepository.saveAll(listOf(notificationDTO))).willReturn(listOf(savedNotificationDTO))

            // When
            repository.insertNotifications(notification)

            // Then
            then(databaseRepository).should(only()).saveAll(listOf(notificationDTO))
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
            val userIdUUID = UUID.randomUUID()
            val userId = userIdUUID.toString()
            given(cacheRepository.getCachedNotificationsForUser(userId)).willReturn(null)
            given(databaseRepository.findAll()).willReturn(emptyList())

            // When
            val result = repository.getUserNotificationList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<Notification>())
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getCachedNotificationsForUser(userId)
                then(databaseRepository).should(it).findAllByUserId(userIdUUID)
                then(cacheRepository).should(it).insertNotificationsToCacheForUser(emptyList(), userId)
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserNotificationList - when cache is initialized and has result - should return mapped result`() {
            // Given
            val userIdUUID = UUID.randomUUID()
            val userId = userIdUUID.toString()
            val notificationDTO = mock(NotificationDTO::class.java)
            given(cacheRepository.getCachedNotificationsForUser(userId))
                .willReturn(listOf(notificationDTO))

            val notification = mock(Notification::class.java)
            given(mapper.toDomain(notificationDTO)).willReturn(notification)

            // When
            val result = repository.getUserNotificationList(userId = userId)

            // Then
            assertThat(result).isEqualTo(listOf(notification))
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getCachedNotificationsForUser(userId)
                then(mapper).should(it).toDomain(notificationDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }
    }
}
