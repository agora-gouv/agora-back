package fr.gouv.agora.infrastructure.consultationUpdates.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "consultation_update_history")
data class ConsultationUpdateHistoryDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val consultationId: String,
    val stepNumber: Int,
    @Column(columnDefinition = "TEXT")
    val type: String,
    @JoinTable(joinColumns = [JoinColumn(table = "consultation_updates_v2", referencedColumnName = "id")])
    val consultationUpdateId: UUID?,
    @Column(columnDefinition = "TEXT")
    val title: String,
    @Column(columnDefinition = "TEXT")
    val actionText: String?,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ConsultationUpdateHistoryDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id=$id, consultationId=$consultationId, stepNumber=$stepNumber, type='$type', consultationUpdateId=$consultationUpdateId, title='$title', actionText='$actionText')"
    }

}
