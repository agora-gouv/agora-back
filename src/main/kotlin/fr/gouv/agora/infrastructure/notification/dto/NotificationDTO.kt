package fr.gouv.agora.infrastructure.notification.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "notifications")
data class NotificationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @Column(columnDefinition = "TEXT")
    val type: String,
    val date: Date,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as NotificationDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + """(id = $id , title = $title , description = $description , date = $date , userId = $userId)"""
    }
}
