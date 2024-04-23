package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "feedbacks_consultation_update")
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_feedbacks_consultation_update_consultationupdateid_userid",
        columnNames = ["consultationUpdateId", "userId"],
    )]
)
data class FeedbackConsultationUpdateDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "consultation_updates_v2", referencedColumnName = "id")])
    val consultationUpdateId: UUID,
    val isPositive: Int,
    val createdDate: Date,
    val updatedDate: Date,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as FeedbackConsultationUpdateDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , userId = $userId , consultationUpdateId = $consultationUpdateId , isPositive = $isPositive , createdDate = $createdDate, updatedDate = $updatedDate )"
    }
}