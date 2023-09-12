package fr.social.gouv.agora.infrastructure.utils

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentLinkedQueue

@Component
class AgoraQueue {

    sealed class TaskType {
        data class AddSupportRequest(val userId: String) : TaskType()
    }

    private val queue = ConcurrentLinkedQueue<TaskType>()

    fun <Result> executeTask(
        taskType: TaskType,
        onTaskExecuted: (suspend () -> Result),
        onTaskRejected: (suspend () -> Result),
    ): Result {
        return runBlocking {
            if (addToQueue(taskType)) {
                val result = onTaskExecuted.invoke()
                removeFromQueue(taskType)
                result
            } else {
                onTaskRejected.invoke()
            }
        }
    }

    private fun addToQueue(taskType: TaskType): Boolean {
        val shouldAddToQueue = !queue.contains(taskType)
        if (shouldAddToQueue) {
            queue.add(taskType)
        }
        return shouldAddToQueue
    }

    private fun removeFromQueue(taskType: TaskType) {
        queue.remove(taskType)
    }

}
