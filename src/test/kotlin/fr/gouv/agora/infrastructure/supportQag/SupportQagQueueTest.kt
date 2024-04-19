package fr.gouv.agora.infrastructure.supportQag

import fr.gouv.agora.infrastructure.supportQag.SupportQagQueue.TaskType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.CompletableFuture

@Suppress("JUnitMalformedDeclaration")
@ExtendWith(MockitoExtension::class)
internal class SupportQagQueueTest {

    private lateinit var queue: SupportQagQueue

    @BeforeEach
    fun setUp() {
        queue = SupportQagQueue()
    }

    @Test
    fun `executeTask - when has no tasks - should return result from onTaskExecuted`() {
        // When
        val result = queue.executeTask(
            taskType = TaskType.AddSupport(userId = "userId"),
            onTaskExecuted = {
                delay(200)
                true
            },
            onTaskRejected = { false },
        )

        // Then
        assertThat(result).isTrue
    }

    @Test
    suspend fun `executeTask - when has identical tasks - should return result from onTaskExecuted then from onTaskRejected`() {
        withContext(Dispatchers.IO) {
            // Given
            val firstResultFuture = CompletableFuture.supplyAsync {
                queue.executeTask(
                    taskType = TaskType.AddSupport(userId = "userId"),
                    onTaskExecuted = {
                        delay(200)
                        true
                    },
                    onTaskRejected = { false },
                )
            }
            val secondResultFuture = CompletableFuture.supplyAsync {
                queue.executeTask(
                    taskType = TaskType.AddSupport(userId = "userId"),
                    onTaskExecuted = {
                        delay(200)
                        true
                    },
                    onTaskRejected = { false },
                )
            }

            // When
            val firstResult = firstResultFuture.get()
            val secondResult = secondResultFuture.get()

            // Then
            assertThat(firstResult).isTrue
            assertThat(secondResult).isFalse
        }
    }

    @Test
    suspend fun `executeTask - when has different tasks - should return result from onTaskExecuted then from onTaskExecuted`() {
        withContext(Dispatchers.IO) {
            // Given
            val firstResultFuture = CompletableFuture.supplyAsync {
                queue.executeTask(
                    taskType = TaskType.AddSupport(userId = "userId1"),
                    onTaskExecuted = {
                        delay(200)
                        true
                    },
                    onTaskRejected = { false },
                )
            }
            val secondResultFuture = CompletableFuture.supplyAsync {
                queue.executeTask(
                    taskType = TaskType.RemoveSupport(userId = "userId2"),
                    onTaskExecuted = {
                        delay(200)
                        true
                    },
                    onTaskRejected = { false },
                )
            }

            // When
            val firstResult = firstResultFuture.get()
            val secondResult = secondResultFuture.get()

            // Then
            assertThat(firstResult).isTrue
            assertThat(secondResult).isTrue
        }
    }

}