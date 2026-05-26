package fr.gouv.agora.infrastructure.themeHebdo

import fr.gouv.agora.usecase.themeHebdo.GetThemeHebdoUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.concurrent.TimeUnit
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Thème Hebdo")
class ThemeHebdoController(
        private val getThemeHebdoUseCase: GetThemeHebdoUseCase,
        private val jsonMapper: ThemeHebdoJsonMapper,
) {
    @GetMapping("/theme_hebdo")
    fun getThemeHebdo(): ResponseEntity<ThemeHebdoJson> {
        val themeHebdo = getThemeHebdoUseCase.getThemeHebdo()
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .body(jsonMapper.toJson(themeHebdo))
    }
}
