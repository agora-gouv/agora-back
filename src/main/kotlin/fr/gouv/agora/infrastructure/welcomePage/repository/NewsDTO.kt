package fr.gouv.agora.infrastructure.welcomePage.repository

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.time.LocalDate
import java.util.*

@Entity(name = "news")
data class NewsDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,

    @Column(columnDefinition = "TEXT")
    val description: String,

    @Column(columnDefinition = "TEXT")
    val callToActionText: String,

    @Column(columnDefinition = "TEXT")
    val routeName: String,

    @Column(columnDefinition = "TEXT")
    val routeArgument: String?,

    val beginDate: LocalDate,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as NewsDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "LastNewsDTO(id=$id, description='$description', callToActionText='$callToActionText', routeName='$routeName', routeArgument='$routeArgument', beginDate=$beginDate)"
    }
}
