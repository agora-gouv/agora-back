package fr.gouv.agora.infrastructure.consultation.dto

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
    val slug: String,
    val startDate: Date,
    val endDate: Date,
    @Column(columnDefinition = "TEXT")
    val coverUrl: String,
    @Column(columnDefinition = "TEXT")
    val detailsCoverUrl: String,
    val questionCountNumber: Int,
    @Column(columnDefinition = "TEXT")
    val questionCount: String,
    @Column(columnDefinition = "TEXT")
    val estimatedTime: String,
    val participantCountGoal: Int,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @Column(columnDefinition = "TEXT")
    val tipsDescription: String,
    @JoinTable(joinColumns = [JoinColumn(table = "thematiques", referencedColumnName = "id")])
    val thematiqueId: UUID,
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
        return this::class.simpleName + "(id = $id , title = $title , startDate = $startDate , endDate = $endDate , " +
                "coverUrl = $coverUrl , detailsCoverUrl = $detailsCoverUrl , questionCountNumber, $questionCountNumber, questionCount = $questionCount , estimatedTime = $estimatedTime , " +
                "participantCountGoal = $participantCountGoal , description = $description , " +
                "tipsDescription = $tipsDescription , thematiqueId = $thematiqueId )"
    }

}
