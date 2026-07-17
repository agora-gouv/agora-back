package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.oninit.WeeklyTasksHandler
import fr.gouv.agora.usecase.cache.ClearCacheUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class AdminTasksController(
    private val weeklyTasksHandler: WeeklyTasksHandler,
    private val clearCacheUseCase: ClearCacheUseCase,
) {

    @Operation(
        summary = "Lancer les tâches hebdomadaires (WeeklyTasksHandler)",
        responses = [
            ApiResponse(responseCode = "200", description = "Tâches hebdomadaires exécutées avec succès"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
        ]
    )
    @PostMapping("/admin/weekly-tasks")
    fun runWeeklyTasks(
        @Parameter(description = "Force la sélection de la question la plus populaire et l'archivage des anciens QaGs, en bypssant la règle de gestion de fin de période de thème hebdomadaire.")
        @RequestParam(defaultValue = "false") force_question_selection: Boolean,
    ): ResponseEntity<String> {
        weeklyTasksHandler.handleTask(mapOf("force_question_selection" to force_question_selection.toString()))
        return ResponseEntity.ok("Weekly tasks exécutées avec succès")
    }

    @Operation(
        summary = "Vider tout le cache Redis",
        responses = [
            ApiResponse(responseCode = "200", description = "Cache Redis vidé avec succès"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
        ]
    )
    @PostMapping("/admin/cache/clear")
    fun clearCache(): ResponseEntity<String> {
        clearCacheUseCase.clearAllCaches()
        return ResponseEntity.ok("Cache Redis entièrement vidé avec succès (flushDb)")
    }
}
