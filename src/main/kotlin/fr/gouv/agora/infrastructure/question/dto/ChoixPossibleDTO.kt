package fr.gouv.agora.infrastructure.question.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.UUID

@Entity(name = "choixpossible")
data class ChoixPossibleDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val label: String,
    val ordre: Int,
    @JoinTable(joinColumns = [JoinColumn(name = "id_question", table = "questions", referencedColumnName = "id")])
    val questionId: UUID,
    @JoinTable(joinColumns = [JoinColumn(name = "id_question", table = "questions", referencedColumnName = "id")])
    val nextQuestionId: UUID?,
    @Column(columnDefinition = "SMALLINT")
    val hasOpenTextField: Int,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChoixPossibleDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id, label = $label, ordre = $ordre, questionId = $questionId, nextQuestionId = $nextQuestionId, hasOpenTextField = $hasOpenTextField)"
    }
}
