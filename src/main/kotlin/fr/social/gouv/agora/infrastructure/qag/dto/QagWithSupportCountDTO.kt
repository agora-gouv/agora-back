package fr.social.gouv.agora.infrastructure.qag.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

data class QagWithSupportCountDTO(
    val id: UUID,
    val title: String,
    val description: String,
    val postDate: Date,
    val status: Int,
    val username: String,
    val thematiqueId: UUID,
    val userId: UUID,
    val supportCount: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as QagWithSupportCountDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + """(id = $id , title = $title , description = $description , postDate = $postDate , 
            status = $status , username = $username , thematiqueId = $thematiqueId , 
            userId = $userId)"""
    }
}
