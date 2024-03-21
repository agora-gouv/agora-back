package fr.gouv.agora.infrastructure.consultationResponse

import fr.gouv.agora.infrastructure.utils.AgoraQueue
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertResponseConsultationQueue : AgoraQueue<InsertResponseConsultationQueue.TaskType>() {

    sealed class TaskType {
        data class InsertResponse(val userId: String) : TaskType()
    }

    override fun canAddTask(queuedTasks: Queue<TaskType>, newTask: TaskType): Boolean {
        return !queuedTasks.contains(newTask)
    }

}