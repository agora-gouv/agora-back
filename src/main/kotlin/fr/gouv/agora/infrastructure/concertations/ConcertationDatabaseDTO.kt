package fr.gouv.agora.infrastructure.concertations

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import java.time.LocalDate
import java.util.UUID

@Entity(name = "concertations")
data class ConcertationDatabaseDTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val title: String,
    @Column(columnDefinition = "TEXT")
    val imageUrl: String,
    @Column(columnDefinition = "TEXT")
    val externalLink: String,
    @Column(columnDefinition = "TEXT")
    val updateLabel: String?,
    val thematiqueId: String,
    val lastUpdateDate: LocalDate,
)
