package fr.gouv.agora.infrastructure.appFeedback.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "app_feedbacks")
data class AppFeedbackDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
    val createdDate: Date,
    val type: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    val deviceModel: String?,
    val osVersion: String?,
    val appVersion: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as AppFeedbackDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , userId = $userId , createdDate = $createdDate , type = $type , description = $description , deviceModel = $deviceModel , osVersion = $osVersion , appVersion = $appVersion )"
    }
}