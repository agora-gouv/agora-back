package fr.gouv.agora.infrastructure.moderatus.dto

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity(name = "moderatus_locked_qags")
data class ModeratusQagLockDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    val lockDate: Date,
    @JoinTable(joinColumns = [JoinColumn(name = "qag_id", table = "qag", referencedColumnName = "id")])
    val qagId: UUID,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModeratusQagLockDTO

        if (qagId != other.qagId) return false

        return true
    }

    override fun hashCode(): Int {
        return qagId.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id, lockDate = $lockDate, qagId = $qagId)"
    }

}