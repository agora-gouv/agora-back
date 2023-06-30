package fr.social.gouv.agora.infrastructure.crons

import fr.social.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.social.gouv.agora.usecase.thematique.ListThematiqueUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ArchivageController(
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
) {
    @GetMapping("/archivage")
    fun getThematiqueList(): ResponseEntity<*> {
        return ResponseEntity.ok()
            .body(archiveOldQagUseCase.archiveOldQag())
    }
}