package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.infrastructure.login.dto.UserDataDTO
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserDataDatabaseRepository : JpaRepository<UserDataDTO, UUID> {}