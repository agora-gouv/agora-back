package fr.gouv.agora.infrastructure.utils

import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

abstract class AgoraQueue<TaskType> {

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

    abstract fun shouldAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean

    private fun addToQueue(taskType: TaskType): Boolean {
        val shouldAddToQueue = shouldAddTask(queue, taskType)
        if (shouldAddToQueue) {
            queue.add(taskType)
        }
        return shouldAddToQueue
    }

    private fun removeFromQueue(taskType: TaskType) {
        queue.remove(taskType)
    }

}
