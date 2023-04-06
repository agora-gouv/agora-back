package fr.social.gouv.agora.infrastructure.consultation.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "consultations")
data class ConsultationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val title: String,
    @Column(columnDefinition = "TEXT")
    val abstract: String,
    val start_date: Date,
    val end_date: Date,
    @Column(columnDefinition = "TEXT")
    val cover: String,
    @Column(columnDefinition = "TEXT")
    val questions_count: String,
    @Column(columnDefinition = "TEXT")
    val estimated_time: String,
    val participant_count_goal: Int,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @Column(columnDefinition = "TEXT")
    val tips_description: String,
    @JoinTable(joinColumns = [JoinColumn(name = "id_thematique", table = "thematiques", referencedColumnName = "id")])
    val id_thematique: UUID,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ConsultationDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , abstract = $abstract , " +
                "start_date = $start_date, end_date=$end_date, cover=$cover, questions_count=$questions_count, " +
                "estimated_time=$estimated_time, participant_count_goal=$participant_count_goal, " +
                "description=$description, tips_description=$tips_description, id_thematique=$id_thematique)"
    }
}