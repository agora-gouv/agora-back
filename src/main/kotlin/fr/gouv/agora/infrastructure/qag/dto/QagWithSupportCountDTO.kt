package fr.gouv.agora.infrastructure.qag.dto

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(types = [QagWithSupportCountDTO::class])
interface QagWithSupportCountDTO {
    val id: UUID
    val title: String
    val description: String
    val postDate: Date
    val status: Int
    val username: String
    val thematiqueId: UUID
    val userId: UUID
    val supportCount: Int
}
