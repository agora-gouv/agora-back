package fr.social.gouv.agora.infrastructure.consultationUpdates.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "consultation_updates")
data class ConsultationUpdateDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    val step: Int,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @JoinTable(joinColumns = [JoinColumn(table = "consultations", referencedColumnName = "id")])
    val consultationId: UUID,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ConsultationUpdateDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , step = $step , description = $description , consultationId = $consultationId )"
    }
}