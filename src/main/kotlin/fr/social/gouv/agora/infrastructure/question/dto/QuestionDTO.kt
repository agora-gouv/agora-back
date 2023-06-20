package fr.social.gouv.agora.infrastructure.question.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "questions")
data class QuestionDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val title: String,
    val ordre: Int,
    @Column(columnDefinition = "TEXT")
    val type: String,
    @Column(columnDefinition = "TEXT")
    val description: String?,
    val maxChoices: Int?,
    val nextQuestionId: UUID?,
    @JoinTable(joinColumns = [JoinColumn(table = "consultations", referencedColumnName = "id")])
    val consultationId: UUID,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as QuestionDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , ordre = $ordre , type = $type, description = $description, maxChoices = $maxChoices, nextQuestionId = $nextQuestionId, consultationId = $consultationId)"
    }
}
