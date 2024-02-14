package fr.gouv.agora.infrastructure.utils

import jakarta.servlet.http.HttpServletRequest

object IpAddressUtils {

    fun retrieveIpAddress(request: HttpServletRequest): String {
        return (
                request.getHeader("X-Forwarded-For")?.takeIf { it.isNotBlank() }
                    ?.let { addresses -> addresses.split(",").firstOrNull { address -> address.trim().isNotBlank() } }
                    ?: request.getHeader("X-Remote-Address")?.takeIf { it.isNotBlank() }
                    ?: request.remoteAddr
                ).trim()
    }

}