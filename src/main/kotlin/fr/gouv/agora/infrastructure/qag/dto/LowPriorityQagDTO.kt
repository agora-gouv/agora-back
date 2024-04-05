package fr.gouv.agora.infrastructure.qag.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "low_priority_qags")
data class LowPriorityQagDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "qags", referencedColumnName = "id")])
    val qagId: UUID,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as LowPriorityQagDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , qagId = $qagId)"
    }
}