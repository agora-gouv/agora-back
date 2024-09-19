package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueDatabaseDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ThematiqueDatabaseRepository : JpaRepository<ThematiqueDatabaseDTO, UUID> {

    @Query(value = "SELECT * FROM thematiques", nativeQuery = true)
    fun getThematiqueList(): List<ThematiqueDatabaseDTO>
}
