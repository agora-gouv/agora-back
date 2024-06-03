package fr.gouv.agora.infrastructure.welcomePage.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NewsDatabaseRepository : JpaRepository<NewsDTO, UUID> {

    @Query(value = "SELECT * FROM last_news", nativeQuery = true)
    fun getNewsList(): List<NewsDTO>
}
