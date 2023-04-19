package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.usecase.supportQag.repository.InsertSupportQagResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class InsertSupportQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: InsertSupportQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: InsertSupportQagDatabaseRepository

    @MockBean
    private lateinit var mapper: SupportQagMapper

    @Test
    fun `insertSupportQag - when mapper returns null - should return FAILURE`() {
        // Given
        val supportQag = mock(SupportQagInserting::class.java)
        given(mapper.toDto(supportQag)).willReturn(null)

        // When
        val result = repository.insertSupportQag(supportQag)

        // Then
        assertThat(result).isEqualTo(InsertSupportQagResult.FAILURE)
    }

    @Test
    fun `insertSupportQag - when mapper returns DTO - should return SUCCESS`() {
        // Given
        val supportQag = mock(SupportQagInserting::class.java)
        given(mapper.toDto(supportQag)).willReturn(mock(SupportQagDTO::class.java))

        // When
        val result = repository.insertSupportQag(supportQag)

        // Then
        assertThat(result).isEqualTo(InsertSupportQagResult.SUCCESS)
    }

    @Test
    fun `insertSupportQag - when mapper returns DTO and ID does not exist - should return SUCCESS`() {
        // Given
        val supportQag = mock(SupportQagInserting::class.java)

        val newSupportQagUUID = UUID.randomUUID()
        val supportQagDTO = mock(SupportQagDTO::class.java).also {
            given(it.id).willReturn(newSupportQagUUID)
        }
        given(mapper.toDto(supportQag)).willReturn(supportQagDTO)



//        given(databaseRepository.findById(supportQag))

        // When
        val result = repository.insertSupportQag(supportQag)

        // Then
        assertThat(result).isEqualTo(InsertSupportQagResult.SUCCESS)
    }

}