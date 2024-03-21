package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.infrastructure.utils.AgoraQueue
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertQagQueue : AgoraQueue<InsertQagQueue.TaskType>() {

    sealed class TaskType {
        data class InsertQag(val userId: String) : TaskType()
    }

    override fun canAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean {
        return !queuedTasks.contains(newTask)
    }

}