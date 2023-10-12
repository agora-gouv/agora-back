package fr.social.gouv.agora.infrastructure.qag.dto

import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Value
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
//data class QagWithSupportCountDTO(
//    val id: UUID,
//    val title: String,
//    val description: String,
//    val postDate: Date,
//    val status: Int,
//    val username: String,
//    val thematiqueId: UUID,
//    val userId: UUID,
//    val supportCount: Int,
//) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
//        other as QagWithSupportCountDTO
//
//        return id == other.id
//    }
//
//    override fun hashCode(): Int = javaClass.hashCode()
//
//    @Override
//    override fun toString(): String {
//        return this::class.simpleName + """(id = $id , title = $title , description = $description , postDate = $postDate ,
//            status = $status , username = $username , thematiqueId = $thematiqueId ,
//            userId = $userId)"""
//    }
//}
