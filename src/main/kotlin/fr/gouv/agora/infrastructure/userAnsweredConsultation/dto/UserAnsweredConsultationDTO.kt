package fr.gouv.agora.infrastructure.userAnsweredConsultation.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "user_answered_consultation")
data class UserAnsweredConsultationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    val participationDate: Date,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "consultations", referencedColumnName = "id")])
    val consultationId: UUID,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserAnsweredConsultationDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , userId = $userId , consultationId = $consultationId )"
    }
}
