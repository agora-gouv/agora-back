package fr.social.gouv.agora.infrastructure.qagUpdates.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "qag_updates")
data class QagUpdatesDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "qags", referencedColumnName = "id")])
    val qagId: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
    val status: Int,
    val moderatedDate: Date,
    val reason: String?,
    val shouldDeleteFlag: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as QagUpdatesDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , qagId = $qagId , userId = $userId , status = $status , moderatedDate = $moderatedDate , reason = $reason , shouldDeleteFlag = $shouldDeleteFlag )"
    }
}
