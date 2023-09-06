package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.TestUtils
import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.QagUpdates
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.*
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ArchiveOldQagUseCaseTest {

    private lateinit var useCase: ArchiveOldQagUseCase

    @MockBean
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var qagUpdatesRepository: QagUpdatesRepository

    @BeforeEach
    fun setUp() {
        useCase = ArchiveOldQagUseCase(
            featureFlagsRepository = featureFlagsRepository,
            qagInfoRepository = qagInfoRepository,
            qagUpdatesRepository = qagUpdatesRepository,
            clock = TestUtils.getFixedClock(LocalDateTime.of(2023, Month.JUNE, 22, 14, 0, 0)),
        )

        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(true)
    }

    @Test
    fun `archiveOldQag - when feature disabled - should do nothing and return FAILURE`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(false)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

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
    fun `archiveOldQag - when has Qag with status MODERATED ACCEPTED and moderatedDate within 14 days - should not archive any QaG and return FAILURE`() {
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
        given(qagUpdatesRepository.getQagUpdates(listOf(qagInfo.id))).willReturn(listOf(qagUpdates))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(qagUpdatesRepository).should(only()).getQagUpdates(listOf(qagInfo.id))
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED ACCEPTED and moderatedDate older than 14 days and the Qag is archived with SUCCESS - should return SUCCESS`() {
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
        given(qagUpdatesRepository.getQagUpdates(listOf(qagId))).willReturn(listOf(qagUpdates))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo.id)
        then(qagInfoRepository).should(times(1)).deleteQagListFromCache(listOf(qagInfo.id))
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(only()).getQagUpdates(listOf(qagId))
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED ACCEPTED and moderatedDate older than 14 days and archiveQag returns FAILURE - should return FAILURE`() {
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
        given(qagUpdatesRepository.getQagUpdates(listOf(qagInfo.id))).willReturn(listOf(qagUpdates))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo.id)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(only()).getQagUpdates(listOf(qagInfo.id))
    }

    @Test
    fun `archiveOldQag - when has two Qag with status MODERATED ACCEPTED and moderatedDate older than 14 days and archiveQag returns FAILURE for one qag and SUCCESS for the other qag - should archive filtered Qags anyway then return FAILURE`() {
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
        given(qagUpdatesRepository.getQagUpdates(listOf(qagInfo1.id, qagInfo2.id))).willReturn(
            listOf(
                qagUpdates1,
                qagUpdates2
            )
        )

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo1.id)
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo2.id)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(times(1)).getQagUpdates(listOf(qagInfo1.id, qagInfo2.id))
        then(qagUpdatesRepository).shouldHaveNoMoreInteractions()
    }


    @Test
    fun `archiveOldQag - when has Qag with status MODERATED REJECTED and moderatedDate is one second after the reset date - should return FAILURE`() {
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
        given(qagUpdatesRepository.getQagUpdates(listOf(qagInfo.id))).willReturn(listOf(qagUpdates))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(qagUpdatesRepository).should(only()).getQagUpdates(listOf(qagInfo.id))
    }

    @Test
    fun `archiveOldQag - when has Qag with status MODERATED REJECTED and moderatedDate is one second before the reset date and archiveQag returns SUCCESS- should return SUCCESS`() {
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
        given(qagUpdatesRepository.getQagUpdates(listOf(qagInfo.id))).willReturn(listOf(qagUpdates))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(qagInfoRepository).should(times(1)).getAllQagInfo()
        then(qagInfoRepository).should(times(1)).archiveQag(qagInfo.id)
        then(qagInfoRepository).should(times(1)).deleteQagListFromCache(listOf(qagInfo.id))
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(qagUpdatesRepository).should(only()).getQagUpdates(listOf(qagInfo.id))
    }

}