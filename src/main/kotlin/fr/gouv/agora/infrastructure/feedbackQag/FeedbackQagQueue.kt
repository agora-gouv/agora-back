package fr.gouv.agora.infrastructure.feedbackQag

import fr.gouv.agora.infrastructure.utils.AgoraQueue
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeedbackQagQueue: AgoraQueue<FeedbackQagQueue.TaskType>() {

    sealed class TaskType {
        data class AddFeedback(val userId: String) : TaskType()
    }

    override fun shouldAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean {
        return !queuedTasks.contains(newTask)
    }
}