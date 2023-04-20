package fr.social.gouv.agora.infrastructure.responseQag.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "responses_qag")
data class ResponseQagDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val author: String,
    @Column(columnDefinition = "TEXT")
    val authorDescription: String,
    val responseDate: Date,
    @Column(columnDefinition = "TEXT")
    val videoUrl: String,
    @Column(columnDefinition = "TEXT")
    val transcription: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ResponseQagDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , author = $author , authorDescription = $authorDescription , responseDate = $responseDate , videoUrl = $videoUrl , transcription = $transcription )"
    }
}