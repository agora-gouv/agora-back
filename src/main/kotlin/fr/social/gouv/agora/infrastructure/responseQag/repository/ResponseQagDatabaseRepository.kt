package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ResponseQagDatabaseRepository : CrudRepository<ResponseQagDTO, UUID> {

    @Query(value = "SELECT * FROM responses_qag", nativeQuery = true)
    fun getAllResponseQagList(): List<ResponseQagDTO>

}