package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.oninit.WeeklyTasksHandler
import fr.gouv.agora.usecase.cache.ClearCacheUseCase
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
    fun runWeeklyTasks(): ResponseEntity<String> {
        weeklyTasksHandler.handleTask(null)
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
        val clearedCacheNames = clearCacheUseCase.clearAllCaches()
        val cacheList = clearedCacheNames.joinToString(separator = "\n") { "- $it" }
        return ResponseEntity.ok("Cache Redis vidé avec succès (${clearedCacheNames.size} cache(s) vidé(s)) :\n$cacheList")
    }
}
