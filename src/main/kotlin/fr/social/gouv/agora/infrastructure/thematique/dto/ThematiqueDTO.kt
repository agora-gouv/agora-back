package fr.social.gouv.agora.infrastructure.thematique.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Entity(name = "thematiques")
data class ThematiqueDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty("id")
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JsonProperty("label")
    @Column(columnDefinition = "TEXT")
    var label: String,
    @JsonProperty("picto")
    @Column(columnDefinition = "TEXT")
    var picto: String,
    @JsonProperty("color")
    @Column(columnDefinition = "TEXT")
    var color: String
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ThematiqueDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , label = $label , picto = $picto , color = $color )"
    }
}
