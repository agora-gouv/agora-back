package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ThematiqueDatabaseRepository : CrudRepository<ThematiqueDTO, Int> {
    @Query(value = "SELECT * FROM \"SCH_AGORA\".thematiques ORDER BY id", nativeQuery = true)
    fun getThematiqueList(): List<ThematiqueDTO>
}
