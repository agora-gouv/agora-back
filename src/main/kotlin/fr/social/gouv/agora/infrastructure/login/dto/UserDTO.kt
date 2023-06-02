package fr.social.gouv.agora.infrastructure.login.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "agora_users")
data class UserDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val password: String,
    @Column(columnDefinition = "TEXT")
    val fcmToken: String,
    val createdDate: Date,
    val authorizationLevel: Int,
    @Column(columnDefinition = "SMALLINT")
    val isBanned: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , password = $password , fcmToken = $fcmToken , createdDate = $createdDate , authorizationLevel = $authorizationLevel , isBanned = $isBanned )"
    }
}