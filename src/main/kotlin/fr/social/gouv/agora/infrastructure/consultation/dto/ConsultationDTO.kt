package fr.social.gouv.agora.infrastructure.consultation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "consultations")
data class ConsultationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id")
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JsonProperty("title")
    @Column(columnDefinition = "TEXT")
    var title: String,
    @JsonProperty("abstract")
    @Column(columnDefinition = "TEXT")
    var abstract: String,
    @JsonProperty("start_date")
    var start_date: Date,
    @JsonProperty("end_date")
    var end_date: Date,
    @JsonProperty("cover")
    @Column(columnDefinition = "TEXT")
    var cover: String,
    @JsonProperty("questions_count")
    @Column(columnDefinition = "TEXT")
    var questions_count: String,
    @JsonProperty("estimated_time")
    @Column(columnDefinition = "TEXT")
    var estimated_time: String,
    @JsonProperty("participant_count_goal")
    var participant_count_goal: Int,
    @JsonProperty("description")
    @Column(columnDefinition = "TEXT")
    var description: String,
    @JsonProperty("tips_description")
    @Column(columnDefinition = "TEXT")
    var tips_description: String,
    @JsonProperty("id_thematique")
    @OneToOne(cascade = [CascadeType.REFRESH])
    @JoinColumn(name = "id_thematique", referencedColumnName = "id")
    var thematique: ThematiqueDTO,
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
                "description=$description, tips_description=$tips_description, id_thematique=${thematique.id})"
    }
}