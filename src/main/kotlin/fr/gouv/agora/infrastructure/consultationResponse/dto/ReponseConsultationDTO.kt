package fr.gouv.agora.infrastructure.consultationResponse.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "reponses_consultation")
data class ReponseConsultationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val consultationId: String,
    @Column(columnDefinition = "TEXT")
    val questionId: String,
    @Column(columnDefinition = "TEXT")
    val choiceId: String?,
    @Column(columnDefinition = "TEXT")
    val responseText: String,
    val participationId: UUID,
    val participationDate: Date,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ReponseConsultationDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , consultationId = $consultationId, questionId = $questionId, choiceId = $choiceId, responseText = $responseText, participationId = $participationId, participationDate = $participationDate, userId = $userId)"
    }
}
