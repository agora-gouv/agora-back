package fr.gouv.agora.infrastructure.login.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "users_data")
data class UserDataDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    val eventType: String,
    val eventDate: Date,
    val userId: String,
    val remoteAddressHash: String,
    val userAgent: String,
    val fcmToken: String,
    val platform: String,
    val versionName: String,
    val versionCode: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserDataDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , eventType = $eventType , eventDate = $eventDate , userId = $userId , remoteAddressHash = $remoteAddressHash , userAgent = $userAgent , fcmToken = $fcmToken , platform = $platform , versionName = $versionName , versionCode = $versionCode )"
    }
}