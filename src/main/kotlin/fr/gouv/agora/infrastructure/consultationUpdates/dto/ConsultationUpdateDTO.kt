package fr.gouv.agora.infrastructure.consultationUpdates.dto

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
    @Column(columnDefinition = "TEXT")
    val explanationsTitle: String?,
    @Column(columnDefinition = "TEXT")
    val videoTitle: String?,
    @Column(columnDefinition = "TEXT")
    val videoIntro: String?,
    @Column(columnDefinition = "TEXT")
    val videoUrl: String?,
    val videoWidth: Int?,
    val videoHeight: Int?,
    @Column(columnDefinition = "TEXT")
    val videoTranscription: String?,
    @Column(columnDefinition = "TEXT")
    val conclusionTitle: String?,
    @Column(columnDefinition = "TEXT")
    val conclusionDescription: String?,
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
        return this::class.simpleName + """(id = $id , step = $step , description = $description , consultationId = $consultationId , 
            explanationsTitle = $explanationsTitle , videoTitle = $videoTitle , videoIntro = $videoIntro , videoUrl = $videoUrl , 
            videoWidth = $videoWidth , videoHeight = $videoHeight , videoTranscription = $videoTranscription , 
            conclusionTitle = $conclusionTitle , conclusionDescription = $conclusionDescription)"""
    }
}