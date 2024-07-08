package fr.gouv.agora.infrastructure.login.dto

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.Hibernate
import java.util.Date
import java.util.UUID

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
    @Column(columnDefinition = "SMALLINT", name = "is_banned")
    val userIsBanned: Int,
    val lastConnectionDate: Date,
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
        return this::class.simpleName + "(id = $id , password = $password , fcmToken = $fcmToken , createdDate = $createdDate , authorizationLevel = $authorizationLevel , isBanned = $userIsBanned , lastConnectionDate = $lastConnectionDate)"
    }
}
