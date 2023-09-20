package fr.social.gouv.agora.infrastructure.supportQag

import fr.social.gouv.agora.infrastructure.utils.AgoraQueue
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupportQagQueue: AgoraQueue<SupportQagQueue.TaskType>() {

    sealed class TaskType {
        data class AddSupport(val userId: String) : TaskType()
        data class RemoveSupport(val userId: String) : TaskType()
    }

    override fun shouldAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean {
        return !queuedTasks.contains(newTask)
    }

}