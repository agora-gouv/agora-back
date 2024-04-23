package fr.gouv.agora.infrastructure.feedbackConsultationUpdate

import fr.gouv.agora.infrastructure.utils.AgoraQueue
import org.springframework.stereotype.Component
import java.util.*

@Component
class FeedbackConsultationUpdateQueue : AgoraQueue<FeedbackConsultationUpdateQueue.TaskType>() {

    sealed class TaskType {
        abstract val userId: String

        data class AddFeedback(override val userId: String) : TaskType()
    }

    override fun canAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean {
        return !queuedTasks.any { queuedTask -> queuedTask.userId == newTask.userId }
    }
}