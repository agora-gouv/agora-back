package fr.gouv.agora.infrastructure.qag.dto

import java.util.Date
import java.util.UUID

interface QagWithSupportCountDTO {
    val id: UUID
    val title: String
    val description: String
    val postDate: Date
    val status: Int
    val username: String
    val thematiqueId: String
    val userId: UUID
    val supportCount: Int
}
