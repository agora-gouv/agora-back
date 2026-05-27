package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.oninit.WeeklyTasksHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class AdminTasksController(
    private val weeklyTasksHandler: WeeklyTasksHandler,
) {

    @Operation(
        summary = "Lancer les tâches hebdomadaires (WeeklyTasksHandler)",
        responses = [
            ApiResponse(responseCode = "200", description = "Tâches hebdomadaires exécutées avec succès"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
        ]
    )
    @PostMapping("/admin/weekly-tasks")
    fun runWeeklyTasks(): ResponseEntity<String> {
        weeklyTasksHandler.handleTask(null)
        return ResponseEntity.ok("Weekly tasks exécutées avec succès")
    }
}
