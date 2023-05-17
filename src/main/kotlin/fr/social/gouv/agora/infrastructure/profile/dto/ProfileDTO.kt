package fr.social.gouv.agora.infrastructure.profile.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.UUID

@Entity(name = "users_profile")
data class ProfileDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val gender: String,
    val yearOfBirth: Int,
    @Column(columnDefinition = "TEXT")
    val department: String,
    @Column(columnDefinition = "TEXT")
    val cityType: String,
    @Column(columnDefinition = "TEXT")
    val jobCategory: String,
    @Column(columnDefinition = "TEXT")
    val voteFrequency: String,
    @Column(columnDefinition = "TEXT")
    val publicMeetingFrequency: String,
    @Column(columnDefinition = "TEXT")
    val consultationFrequency: String,
    @JoinTable(joinColumns = [JoinColumn(table = "agora_users", referencedColumnName = "id")])
    val userId: UUID,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProfileDTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + """(id = $id , gender = $gender, yearOfBirth = $yearOfBirth , 
            |department = $department , cityType = $cityType , jobCategory=$jobCategory, 
            |voteFrequency = $voteFrequency , publicMeetingFrequency = $publicMeetingFrequency , 
            |consultationFrequency = $consultationFrequency , userId = $userId)"""
    }
}