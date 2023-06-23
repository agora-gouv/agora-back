package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.QagUpdates
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.qag.repository.QagArchiveResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.*
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ArchiveOldQagUseCaseTest {

    @Autowired
    private lateinit var useCase: ArchiveOldQagUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var qagUpdatesRepository: QagUpdatesRepository

    @BeforeEach
    fun setUpDate() = setupNowDate(LocalDateTime.of(2023, Month.JUNE, 22, 14, 0, 0))

    @Test
    fun `archiveOldQag - when no Qag - should return FAILURE`() {
        // Given
        given(qagInfoRepository.getAllQagInfo()).willReturn(emptyList())

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `archiveOldQag - when has Qag with status different that MODERATED ACCEPTED or REJETED - should return FAILURE`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also { given(it.status).willReturn(QagStatus.OPEN) }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED ACCEPTED and posteDate within 14 days - should return FAILURE`() {
        // Given
        val qagId = UUID.randomUUID().toString()
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        val qagUpdates = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 10, 15, 0, 0).toDate())
        }

        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(qagUpdatesRepository.getQagUpdates(qagInfo.id)).willReturn(qagUpdates)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(qagUpdatesRepository).should(only()).getQagUpdates(qagInfo.id)
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED ACCEPTED and postDate older than 14 days and the Qag is archived with SUCCESS - should return SUCCESS`() {
        // Given
        val qagId = UUID.randomUUID().toString()
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        val qagUpdates = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 1, 15, 0, 0).toDate())
        }

        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(qagInfoRepository.archiveQag(qagInfo.id)).willReturn(QagArchiveResult.SUCCESS)
        given(qagUpdatesRepository.getQagUpdates(qagId)).willReturn(qagUpdates)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo.id)
        then(qagInfoRepository).should(times(1)).deleteQagListFromCache(listOf(qagInfo.id))
        then(qagUpdatesRepository).should(only()).getQagUpdates(qagId)
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED ACCEPTED and postDate older than 14 days and archiveQag returns FAILURE - should return FAILURE`() {
        // Given
        val qagId = UUID.randomUUID().toString()
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        val qagUpdates = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 1, 15, 0, 0).toDate())
        }

        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(qagInfoRepository.archiveQag(qagInfo.id)).willReturn(QagArchiveResult.FAILURE)
        given(qagUpdatesRepository.getQagUpdates(qagInfo.id)).willReturn(qagUpdates)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo.id)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(only()).getQagUpdates(qagInfo.id)
    }

    @Test
    fun `archiveOldQag - when has two Qag with status MODERATED ACCEPTED and postDate older than 14 days and archiveQag returns FAILURE for one qag and SUCCESS for the other qag - should return FAILURE`() {
        // Given
        val qagId1 = UUID.randomUUID().toString()
        val qagId2 = UUID.randomUUID().toString()
        val qagInfo1 = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId1)
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        val qagInfo2 = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId2)
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        val qagUpdates1 = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId1)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 1, 15, 0, 0).toDate())
        }
        val qagUpdates2 = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId2)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 1, 15, 0, 0).toDate())
        }

        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo1, qagInfo2))
        given(qagInfoRepository.archiveQag(qagInfo1.id)).willReturn(QagArchiveResult.FAILURE)
        given(qagInfoRepository.archiveQag(qagInfo2.id)).willReturn(QagArchiveResult.SUCCESS)
        given(qagUpdatesRepository.getQagUpdates(qagInfo1.id)).willReturn(qagUpdates1)
        given(qagUpdatesRepository.getQagUpdates(qagInfo2.id)).willReturn(qagUpdates2)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo1.id)
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo2.id)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(times(1)).getQagUpdates(qagInfo1.id)
        then(qagUpdatesRepository).should(times(1)).getQagUpdates(qagInfo2.id)
        then(qagUpdatesRepository).shouldHaveNoMoreInteractions()
    }


    @Test
    fun `archiveOldQag - when has Qag with status MODERATED REJECTED and postDate is one second after the reset date - should return FAILURE`() {
        // Given
        val qagId = UUID.randomUUID().toString()
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
        }
        val qagUpdates = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 8, 14, 0, 1).toDate())
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(qagUpdatesRepository.getQagUpdates(qagInfo.id)).willReturn(qagUpdates)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(qagUpdatesRepository).should(only()).getQagUpdates(qagInfo.id)
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED REJECTED and postDate is one second before the reset date and archiveQag returns SUCCESS- should return SUCCESS`() {
        // Given
        val qagId = UUID.randomUUID().toString()
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.status).willReturn(QagStatus.MODERATED_REJECTED)
        }
        val qagUpdates = mock(QagUpdates::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.moderatedDate).willReturn(LocalDateTime.of(2023, Month.JUNE, 8, 13, 59, 59).toDate())
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(qagInfoRepository.archiveQag(qagInfo.id)).willReturn(QagArchiveResult.SUCCESS)
        given(qagUpdatesRepository.getQagUpdates(qagInfo.id)).willReturn(qagUpdates)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo.id)
        then(qagInfoRepository).should(times(1)).deleteQagListFromCache(listOf(qagInfo.id))
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(only()).getQagUpdates(qagInfo.id)
    }

    private fun setupNowDate(dateTime: LocalDateTime) {
        useCase = ArchiveOldQagUseCase(
            qagInfoRepository = qagInfoRepository,
            qagUpdatesRepository = qagUpdatesRepository,
            clock = Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()),
        )
    }
}