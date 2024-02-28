package fr.gouv.agora.infrastructure.consultationUpdates.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "consultation_update_sections")
data class ConsultationUpdateSectionDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "consultation_updates_v2", referencedColumnName = "id")])
    val consultationUpdateId: UUID,
    @Column(columnDefinition = "TEXT")
    val type: String,
    val ordre: Int,
    val isPreview: Int,
    @Column(columnDefinition = "TEXT")
    val title: String?,
    @Column(columnDefinition = "TEXT")
    val description: String?,
    @Column(columnDefinition = "TEXT")
    val url: String?,
    val videoWidth: Int?,
    val videoHeight: Int?,
    @Column(columnDefinition = "TEXT")
    val authorInfoName: String?,
    @Column(columnDefinition = "TEXT")
    val authorInfoMessage: String?,
    val videoDate: Date?,
    @Column(columnDefinition = "TEXT")
    val videoTranscription: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ConsultationUpdateSectionDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , consultationUpdateId = $consultationUpdateId , type = $type , ordre = $ordre , isPreview = $isPreview , title = $title , description = $description , url = $url , videoWidth = $videoWidth , videoHeight = $videoHeight , authorInfoName = $authorInfoName , authorInfoMessage = $authorInfoMessage , videoDate = $videoDate , videoTranscription = $videoTranscription )"
    }
}
