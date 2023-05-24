package fr.social.gouv.agora.infrastructure.qag.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "qags")
data class QagDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    val postDate: Date,
    val status: Int,
    @Column(columnDefinition = "TEXT")
    val username: String,
    @JoinTable(joinColumns = [JoinColumn(table = "thematiques", referencedColumnName = "id")])
    val thematiqueId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as QagDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , description = $description , postDate = $postDate , status = $status , username = $username , thematiqueId = $thematiqueId , userId = $userId)"
    }

}
