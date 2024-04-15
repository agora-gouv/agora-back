package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.infrastructure.moderatus.ModerateQagOptionsMapper.Result
import fr.gouv.agora.usecase.moderatus.ModerateQagOptions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ModerateQagOptionsMapperTest {

    @InjectMocks
    private lateinit var mapper: ModerateQagOptionsMapper

    private val expectedModerateQagOptions = ModerateQagOptions(
        qagId = "qagId",
        userId = "userId",
        isAccepted = true,
        reason = null,
        shouldDelete = false,
    )

    @Test
    fun `toModerateQagOptions - when status is invalid - should return error`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "Coucou !",
            reason = null,
            shouldDeleteFlag = null,
        )

        // Then
        assertThat(result).isEqualTo(Result.Error(errorMessage = "Status invalide"))
    }

    @Test
    fun `toModerateQagOptions - when status is OK - should return isAccepted equals to true`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "OK",
            reason = "Parce que !",
            shouldDeleteFlag = null,
        )

        // Then
        assertThat(result).isEqualTo(
            Result.Success(
                expectedModerateQagOptions.copy(
                    isAccepted = true,
                    reason = "Parce que !",
                )
            )
        )
    }

    @Test
    fun `toModerateQagOptions - when status is NOK - should return isAccepted equals to false`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "NOK",
            reason = "Comme ça",
            shouldDeleteFlag = 0,
        )

        // Then
        assertThat(result).isEqualTo(
            Result.Success(
                expectedModerateQagOptions.copy(
                    isAccepted = false,
                    reason = "Comme ça",
                )
            )
        )
    }

    @Test
    fun `toModerateQagOptions - when shouldDeleteFlag is null - should return shouldDeleteFlag equals to false`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "OK",
            reason = null,
            shouldDeleteFlag = null,
        )

        // Then
        assertThat(result).isEqualTo(
            Result.Success(
                expectedModerateQagOptions.copy(shouldDelete = false)
            )
        )
    }

    @Test
    fun `toModerateQagOptions - when shouldDeleteFlag is other than 1 or null - should return shouldDeleteFlag equals to false`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "OK",
            reason = null,
            shouldDeleteFlag = 56,
        )

        // Then
        assertThat(result).isEqualTo(
            Result.Success(
                expectedModerateQagOptions.copy(shouldDelete = false)
            )
        )
    }

    @Test
    fun `toModerateQagOptions - when shouldDeleteFlag is 1 and status is NOK - should return shouldDeleteFlag equals to true`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "NOK",
            reason = null,
            shouldDeleteFlag = 1,
        )

        // Then
        assertThat(result).isEqualTo(
            Result.Success(
                expectedModerateQagOptions.copy(
                    isAccepted = false,
                    shouldDelete = true
                )
            )
        )
    }

    @Test
    fun `toModerateQagOptions - when shouldDeleteFlag is 1 but status is OK - should return shouldDeleteFlag equals to false`() {
        // When
        val result = mapper.toModerateQagOptions(
            qagId = "qagId",
            userId = "userId",
            status = "OK",
            reason = null,
            shouldDeleteFlag = 1,
        )

        // Then
        assertThat(result).isEqualTo(
            Result.Success(
                expectedModerateQagOptions.copy(
                    isAccepted = true,
                    shouldDelete = false,
                )
            )
        )
    }

}