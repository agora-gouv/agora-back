package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagLockResult
import fr.social.gouv.agora.domain.QagLockResult
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class LockModeratusQagListUseCaseTest {

    @Autowired
    private lateinit var useCase: LockModeratusQagListUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var moderatusQagLockRepository: ModeratusQagLockRepository

    @Test
    fun `lockQagIds - when lockedQagIds is empty - should return emptyList and don't add lockedQagIds`() {
        // When
        val result = useCase.lockQagIds(lockedQagIds = emptyList())

        // Then
        assertThat(result).isEqualTo(emptyList<ModeratusQagLockResult>())
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(moderatusQagLockRepository).should(only()).getLockedQagIds()
    }

    @Test
    fun `lockQagIds - when has no QaG info - should return NOT_FOUND and don't add lockedQagIds`() {
        // Given
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.lockQagIds(lockedQagIds = listOf("qagId"))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ModeratusQagLockResult(
                    qagId = "qagId",
                    lockResult = QagLockResult.NOT_FOUND,
                )
            )
        )
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(moderatusQagLockRepository).should(only()).getLockedQagIds()
    }

    @Test
    fun `lockQagIds - when has QaG info but not status OPEN - should return NOT_FOUND and don't add lockedQagIds`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.ARCHIVED)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)

        // When
        val result = useCase.lockQagIds(lockedQagIds = listOf("qagId"))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ModeratusQagLockResult(
                    qagId = "qagId",
                    lockResult = QagLockResult.NOT_FOUND,
                )
            )
        )
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(moderatusQagLockRepository).should(only()).getLockedQagIds()
    }

    @Test
    fun `lockQagIds - when has QaG info, status OPEN but is already locked for moderatus - should return NOT_FOUND and don't add lockedQagIds`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.ARCHIVED)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(moderatusQagLockRepository.getLockedQagIds()).willReturn(listOf("qagId"))

        // When
        val result = useCase.lockQagIds(lockedQagIds = listOf("qagId"))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ModeratusQagLockResult(
                    qagId = "qagId",
                    lockResult = QagLockResult.NOT_FOUND,
                )
            )
        )
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(moderatusQagLockRepository).should(only()).getLockedQagIds()
    }

    @Test
    fun `lockQagIds - when has QaG info, status OPEN, is not moderatus locked - should return SUCCESS and add lockedQagIds`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(moderatusQagLockRepository.getLockedQagIds()).willReturn(emptyList())

        // When
        val result = useCase.lockQagIds(lockedQagIds = listOf("qagId"))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ModeratusQagLockResult(
                    qagId = "qagId",
                    lockResult = QagLockResult.SUCCESS,
                )
            )
        )
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(moderatusQagLockRepository).should().getLockedQagIds()
        then(moderatusQagLockRepository).should().addLockedIds(listOf("qagId"))
        then(moderatusQagLockRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `lockQagIds - when has duplicates - should process only one of them`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.status).willReturn(QagStatus.OPEN)
        }
        given(qagInfoRepository.getQagInfo(qagId = "qagId")).willReturn(qagInfo)
        given(moderatusQagLockRepository.getLockedQagIds()).willReturn(emptyList())


        // When
        val result = useCase.lockQagIds(lockedQagIds = listOf("qagId", "qagId", "qagId"))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ModeratusQagLockResult(
                    qagId = "qagId",
                    lockResult = QagLockResult.SUCCESS,
                )
            )
        )
        then(qagInfoRepository).should(only()).getQagInfo(qagId = "qagId")
        then(moderatusQagLockRepository).should().getLockedQagIds()
        then(moderatusQagLockRepository).should().addLockedIds(listOf("qagId"))
        then(moderatusQagLockRepository).shouldHaveNoMoreInteractions()
    }

}