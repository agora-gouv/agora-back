package fr.gouv.agora.infrastructure.consultationAggregate.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "consultation_results")
data class ConsultationResultAggregatedDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val consultationId: String,
    @Column(columnDefinition = "TEXT")
    val questionId: String,
    @Column(columnDefinition = "TEXT")
    val choiceId: String,
    val responseCount: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ConsultationResultAggregatedDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + """(id = $id , consultationId = $consultationId , questionId = $questionId , 
            choiceId = $choiceId , responseCount = $responseCount)"""
    }
}
