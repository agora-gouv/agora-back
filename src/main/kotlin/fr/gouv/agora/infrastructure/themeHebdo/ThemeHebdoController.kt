package fr.gouv.agora.infrastructure.themeHebdo

import fr.gouv.agora.usecase.themeHebdo.GetThemeHebdoUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.CacheControl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@Tag(name = "Thème Hebdo")
class ThemeHebdoController(
    private val getThemeHebdoUseCase: GetThemeHebdoUseCase,
    private val jsonMapper: ThemeHebdoJsonMapper,
) {
    @GetMapping("/theme_hebdo")
    fun getThemeHebdo(): ResponseEntity<ThemeHebdoJson> {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES).cachePublic())
            .body(jsonMapper.toJson(getThemeHebdoUseCase.getThemeHebdo()))
    }
}
