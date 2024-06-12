package fr.gouv.agora.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server


@OpenAPIDefinition(
    info = Info(
        title = "Code-First Approach",
        description = "Spring Doc Sample",
    ), servers = [Server(url = "http://localhost:8080")]
)
@SecurityScheme(name = "api", scheme = "basic", type = SecuritySchemeType.HTTP, `in` = SecuritySchemeIn.HEADER)
class OpenAPIConfiguration
