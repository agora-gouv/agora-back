package fr.social.gouv.agora.infrastructure.profile.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "demographic_info_ask_date")
data class DemographicInfoAskDateDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    val askDate: Date,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DemographicInfoAskDateDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , askDate = $askDate , userId = $userId )"
    }

}