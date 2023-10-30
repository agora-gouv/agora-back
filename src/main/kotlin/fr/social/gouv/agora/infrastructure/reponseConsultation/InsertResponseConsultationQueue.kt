package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.infrastructure.utils.AgoraQueue
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertResponseConsultationQueue : AgoraQueue<InsertResponseConsultationQueue.TaskType>() {

    sealed class TaskType {
        data class InsertResponse(val userId: String) : TaskType()
    }

    override fun shouldAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean {
        return !queuedTasks.contains(newTask)
    }

}