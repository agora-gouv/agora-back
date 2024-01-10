package fr.gouv.agora.infrastructure.consultationUpdates.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "explanations")
data class ExplanationDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    val ordre: Int,
    @Column(columnDefinition = "TEXT")
    val title: String,
    @Column(columnDefinition = "TEXT")
    val intro: String,
    @Column(columnDefinition = "TEXT")
    val imageUrl: String?,
    @Column(columnDefinition = "TEXT")
    val imageDescription: String?,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @JoinTable(joinColumns = [JoinColumn(table = "consultation_updates", referencedColumnName = "id")])
    val consultationUpdatesId: UUID,
    @Column(columnDefinition = "TEXT")
    val toggleable: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ExplanationDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , ordre = $ordre , title = $title , intro = $intro , imageUrl = $imageUrl , description = $description , consultationUpdatesId = $consultationUpdatesId , toggleable = $toggleable )"
    }
}