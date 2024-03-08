package fr.gouv.agora.infrastructure.feedbackConsultationUpdate.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "feedbacks_consultation_update")
data class FeedbackConsultationUpdateDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "consultation_updates_v2", referencedColumnName = "id")])
    val consultationUpdateId: UUID,
    @Column(columnDefinition = "SMALLINT")
    val isPositive: Int,
    val createdDate: Date,
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
        return this::class.simpleName + "(id = $id , userId = $userId , consultationUpdateId = $consultationUpdateId , isPositive = $isPositive , createdDate = $createdDate)"
    }
}