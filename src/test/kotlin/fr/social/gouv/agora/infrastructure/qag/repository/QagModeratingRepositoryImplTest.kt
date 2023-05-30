package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.usecase.qag.repository.QagModeratingInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class QagModeratingRepositoryImplTest {

    @Autowired
    private lateinit var repository: QagModeratingRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: QagInfoDatabaseRepository

    @MockBean
    private lateinit var mapper: QagModeratingInfoMapper

    private val qagDTO = QagDTO(
        id = UUID.randomUUID(),
        title = "title",
        description = "description",
        postDate = Date(42),
        status = 256,
        username = "username",
        thematiqueId = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        isAccepted = 1,
    )

    private val qagModeratingInfo = QagModeratingInfo(
        id = "id",
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        username = "username",
        date = Date(14),
    )

    @Test
    fun `getQagModeratingList - when database return emptyList - should return emptyList without doing anything`() {
        //Given
        given(databaseRepository.getQagModeratingList()).willReturn(emptyList())

        // When
        val result = repository.getQagModeratingList()

        // Then
        assertThat(result).isEqualTo(emptyList<QagModeratingInfo>())
        then(databaseRepository).should(only()).getQagModeratingList()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagModeratingList - when database return List DTO - should returned mapped DTO`() {
        // Given
        given(databaseRepository.getQagModeratingList()).willReturn(listOf(qagDTO))
        given(mapper.toDomain(qagDTO)).willReturn(qagModeratingInfo)

        // When
        val result = repository.getQagModeratingList()

        // Then
        assertThat(result).isEqualTo(listOf(qagModeratingInfo))
        then(databaseRepository).should(only()).getQagModeratingList()
        then(mapper).should(only()).toDomain(qagDTO)
    }

    @Test
    fun `getModeratingQagCount - should return ModeratingQagCount from database`() {
        // Given
        given(databaseRepository.getModeratingQagCount()).willReturn(40)

        // When
        val result = repository.getModeratingQagCount()

        // Then
        assertThat(result).isEqualTo(40)
        then(mapper).shouldHaveNoInteractions()
    }
}