package fr.social.gouv.agora.infrastructure.reponseConsultation.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.UUID

@Entity(name = "reponses_consultation")
data class ReponseConsultationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "consultations", referencedColumnName = "id")])
    val consultationId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "questions", referencedColumnName = "id")])
    val questionId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "choixpossible", referencedColumnName = "id")])
    val choiceId: UUID ?,
    @Column(columnDefinition = "TEXT")
    val responseText: String,
    val participationId: UUID,
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
        return this::class.simpleName + "(id = $id , consultationId = $consultationId, questionId = $questionId, choiceId = $choiceId, responseText = $responseText, participationId = $participationId)"
    }
}
