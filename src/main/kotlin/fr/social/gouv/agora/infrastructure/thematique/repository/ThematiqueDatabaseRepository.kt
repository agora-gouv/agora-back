package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface ThematiqueDatabaseRepository : CrudRepository<ThematiqueDTO, UUID> {

    @Query(value = "SELECT * FROM thematiques", nativeQuery = true)
    fun getThematiqueList(): List<ThematiqueDTO>
}
