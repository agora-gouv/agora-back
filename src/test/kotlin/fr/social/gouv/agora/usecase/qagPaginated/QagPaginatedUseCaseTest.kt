package fr.social.gouv.agora.usecase.qagPaginated

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.social.gouv.agora.usecase.qagPaginated.repository.QagDateFreezeRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
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
internal class QagPaginatedUseCaseTest {

    @Autowired
    private lateinit var useCase: QagPaginatedUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var supportQagRepository: GetSupportQagRepository

    @MockBean
    private lateinit var dateFreezeRepository: QagDateFreezeRepository

    @MockBean
    private lateinit var mapper: QagPreviewMapper

    private val userId = "userId"
    private val thematiqueId = "thematiqueId"

    @Nested
    inner class PopularQagPaginatedTestCases {

        @Test
        fun `getPopularQagPaginated - when pageNumber is lower or equals 0 - should return null`() {
            // When
            val result = useCase.getPopularQagPaginated(
                userId = userId,
                pageNumber = 0,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(null)
            then(qagInfoRepository).shouldHaveNoInteractions()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportQagRepository).shouldHaveNoInteractions()
            then(dateFreezeRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getPopularQagPaginated - when pageNumber is higher than maxPageNumber - should return null`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(16)

            // When
            val result = useCase.getPopularQagPaginated(
                userId = userId,
                pageNumber = 2,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(null)
            then(qagInfoRepository).should(only()).getQagsCount()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportQagRepository).shouldHaveNoInteractions()
            then(dateFreezeRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getPopularQagPaginated - when pageNumber is 1 but has only qags without matching thematique - should init dateFreeze then return emptyList`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(1)

            val dateFreeze = mock(Date::class.java)
            given(dateFreezeRepository.initQagDateFreeze(userId = userId, thematiqueId = thematiqueId))
                .willReturn(dateFreeze)

            val qag = mock(QagInfoWithSupportCount::class.java).also {
                given(it.thematiqueId).willReturn(thematiqueId)
            }
            given(
                qagInfoRepository.getPopularQagsPaginated(
                    thematiqueId = thematiqueId,
                    maxDate = dateFreeze,
                    offset = 0,
                )
            ).willReturn(listOf(qag))

            given(thematiqueRepository.getThematiqueList()).willReturn(emptyList())
            given(supportQagRepository.getUserSupportedQags(userId = userId)).willReturn(listOf("qagId"))

            // When
            val result = useCase.getPopularQagPaginated(
                userId = userId,
                pageNumber = 1,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(
                QagsAndMaxPageCount(
                    qags = emptyList(),
                    maxPageCount = 1,
                )
            )
            then(qagInfoRepository).should().getQagsCount()
            then(qagInfoRepository).should().getPopularQagsPaginated(
                thematiqueId = thematiqueId,
                maxDate = dateFreeze,
                offset = 0,
            )
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
            then(thematiqueRepository).should(only()).getThematiqueList()
            then(supportQagRepository).should(only()).getUserSupportedQags(userId = userId)
            then(dateFreezeRepository).should(only()).initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getPopularQagPaginated - when pageNumber is 2 and has qags with matching thematique - should get dateFreeze from repository then return mapped results`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(27)

            val dateFreeze = mock(Date::class.java)
            given(dateFreezeRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId))
                .willReturn(dateFreeze)

            val qag = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.thematiqueId).willReturn(thematiqueId)
                given(it.userId).willReturn("anotherUserId")
            }
            given(
                qagInfoRepository.getPopularQagsPaginated(
                    thematiqueId = thematiqueId,
                    maxDate = dateFreeze,
                    offset = 20,
                )
            ).willReturn(listOf(qag))

            val thematique = mock(Thematique::class.java).also {
                given(it.id).willReturn(thematiqueId)
            }
            given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
            given(supportQagRepository.getUserSupportedQags(userId = userId)).willReturn(listOf("qagId"))

            // When
            val result = useCase.getPopularQagPaginated(
                userId = userId,
                pageNumber = 2,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(
                QagsAndMaxPageCount(
                    qags = emptyList(),
                    maxPageCount = 2,
                )
            )
            then(qagInfoRepository).should().getQagsCount()
            then(qagInfoRepository).should().getPopularQagsPaginated(
                thematiqueId = thematiqueId,
                maxDate = dateFreeze,
                offset = 20,
            )
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
            then(thematiqueRepository).should(only()).getThematiqueList()
            then(supportQagRepository).should(only()).getUserSupportedQags(userId = userId)
            then(dateFreezeRepository).should(only()).getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
            then(mapper).should()
                .toPreview(qag = qag, thematique = thematique, isSupportedByUser = true, isAuthor = false)
        }

    }

    @Nested
    inner class LatestQagPaginatedTestCases {

        @Test
        fun `getLatestQagPaginated - when pageNumber is lower or equals 0 - should return null`() {
            // When
            val result = useCase.getLatestQagPaginated(
                userId = userId,
                pageNumber = 0,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(null)
            then(qagInfoRepository).shouldHaveNoInteractions()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportQagRepository).shouldHaveNoInteractions()
            then(dateFreezeRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getLatestQagPaginated - when pageNumber is higher than maxPageNumber - should return null`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(16)

            // When
            val result = useCase.getLatestQagPaginated(
                userId = userId,
                pageNumber = 2,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(null)
            then(qagInfoRepository).should(only()).getQagsCount()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportQagRepository).shouldHaveNoInteractions()
            then(dateFreezeRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getLatestQagPaginated - when pageNumber is 1 but has only qags without matching thematique - should init dateFreeze then return emptyList`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(1)

            val dateFreeze = mock(Date::class.java)
            given(dateFreezeRepository.initQagDateFreeze(userId = userId, thematiqueId = thematiqueId))
                .willReturn(dateFreeze)

            val qag = mock(QagInfoWithSupportCount::class.java).also {
                given(it.thematiqueId).willReturn(thematiqueId)
            }
            given(
                qagInfoRepository.getLatestQagsPaginated(
                    thematiqueId = thematiqueId,
                    maxDate = dateFreeze,
                    offset = 0,
                )
            ).willReturn(listOf(qag))

            given(thematiqueRepository.getThematiqueList()).willReturn(emptyList())
            given(supportQagRepository.getUserSupportedQags(userId = userId)).willReturn(listOf("qagId"))

            // When
            val result = useCase.getLatestQagPaginated(
                userId = userId,
                pageNumber = 1,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(
                QagsAndMaxPageCount(
                    qags = emptyList(),
                    maxPageCount = 1,
                )
            )
            then(qagInfoRepository).should().getQagsCount()
            then(qagInfoRepository).should().getLatestQagsPaginated(
                thematiqueId = thematiqueId,
                maxDate = dateFreeze,
                offset = 0,
            )
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
            then(thematiqueRepository).should(only()).getThematiqueList()
            then(supportQagRepository).should(only()).getUserSupportedQags(userId = userId)
            then(dateFreezeRepository).should(only()).initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getLatestQagPaginated - when pageNumber is 2 and has qags with matching thematique - should get dateFreeze from repository then return mapped results`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(27)

            val dateFreeze = mock(Date::class.java)
            given(dateFreezeRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId))
                .willReturn(dateFreeze)

            val qag = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.thematiqueId).willReturn(thematiqueId)
                given(it.userId).willReturn("anotherUserId")
            }
            given(
                qagInfoRepository.getLatestQagsPaginated(
                    thematiqueId = thematiqueId,
                    maxDate = dateFreeze,
                    offset = 20,
                )
            ).willReturn(listOf(qag))

            val thematique = mock(Thematique::class.java).also {
                given(it.id).willReturn(thematiqueId)
            }
            given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
            given(supportQagRepository.getUserSupportedQags(userId = userId)).willReturn(listOf("qagId"))

            // When
            val result = useCase.getLatestQagPaginated(
                userId = userId,
                pageNumber = 2,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(
                QagsAndMaxPageCount(
                    qags = emptyList(),
                    maxPageCount = 2,
                )
            )
            then(qagInfoRepository).should().getQagsCount()
            then(qagInfoRepository).should().getLatestQagsPaginated(
                thematiqueId = thematiqueId,
                maxDate = dateFreeze,
                offset = 20,
            )
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
            then(thematiqueRepository).should(only()).getThematiqueList()
            then(supportQagRepository).should(only()).getUserSupportedQags(userId = userId)
            then(dateFreezeRepository).should(only()).getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
            then(mapper).should()
                .toPreview(qag = qag, thematique = thematique, isSupportedByUser = true, isAuthor = false)
        }

    }

    @Nested
    inner class SupportedQagPaginatedTestCases {

        @Test
        fun `getSupportedQagPaginated - when pageNumber is lower or equals 0 - should return null`() {
            // When
            val result = useCase.getSupportedQagPaginated(
                userId = userId,
                pageNumber = 0,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(null)
            then(qagInfoRepository).shouldHaveNoInteractions()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportQagRepository).shouldHaveNoInteractions()
            then(dateFreezeRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getSupportedQagPaginated - when pageNumber is higher than maxPageNumber - should return null`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(16)

            // When
            val result = useCase.getSupportedQagPaginated(
                userId = userId,
                pageNumber = 2,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(null)
            then(qagInfoRepository).should(only()).getQagsCount()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportQagRepository).shouldHaveNoInteractions()
            then(dateFreezeRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getSupportedQagPaginated - when pageNumber is 1 but has only qags without matching thematique - should init dateFreeze then return emptyList`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(1)

            val dateFreeze = mock(Date::class.java)
            given(dateFreezeRepository.initQagDateFreeze(userId = userId, thematiqueId = thematiqueId))
                .willReturn(dateFreeze)

            val qag = mock(QagInfoWithSupportCount::class.java).also {
                given(it.thematiqueId).willReturn(thematiqueId)
            }
            given(
                qagInfoRepository.getSupportedQagsPaginated(
                    userId = userId,
                    thematiqueId = thematiqueId,
                    maxDate = dateFreeze,
                    offset = 0,
                )
            ).willReturn(listOf(qag))

            given(thematiqueRepository.getThematiqueList()).willReturn(emptyList())
            given(supportQagRepository.getUserSupportedQags(userId = userId)).willReturn(listOf("qagId"))

            // When
            val result = useCase.getSupportedQagPaginated(
                userId = userId,
                pageNumber = 1,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(
                QagsAndMaxPageCount(
                    qags = emptyList(),
                    maxPageCount = 1,
                )
            )
            then(qagInfoRepository).should().getQagsCount()
            then(qagInfoRepository).should().getSupportedQagsPaginated(
                userId = userId,
                thematiqueId = thematiqueId,
                maxDate = dateFreeze,
                offset = 0,
            )
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
            then(thematiqueRepository).should(only()).getThematiqueList()
            then(supportQagRepository).should(only()).getUserSupportedQags(userId = userId)
            then(dateFreezeRepository).should(only()).initQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getSupportedQagPaginated - when pageNumber is 2 and has qags with matching thematique - should get dateFreeze from repository then return mapped results`() {
            // Given
            given(qagInfoRepository.getQagsCount()).willReturn(27)

            val dateFreeze = mock(Date::class.java)
            given(dateFreezeRepository.getQagDateFreeze(userId = userId, thematiqueId = thematiqueId))
                .willReturn(dateFreeze)

            val qag = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.thematiqueId).willReturn(thematiqueId)
                given(it.userId).willReturn("anotherUserId")
            }
            given(
                qagInfoRepository.getSupportedQagsPaginated(
                    userId = userId,
                    thematiqueId = thematiqueId,
                    maxDate = dateFreeze,
                    offset = 20,
                )
            ).willReturn(listOf(qag))

            val thematique = mock(Thematique::class.java).also {
                given(it.id).willReturn(thematiqueId)
            }
            given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
            given(supportQagRepository.getUserSupportedQags(userId = userId)).willReturn(listOf("qagId"))

            // When
            val result = useCase.getSupportedQagPaginated(
                userId = userId,
                pageNumber = 2,
                thematiqueId = thematiqueId,
            )

            // Then
            assertThat(result).isEqualTo(
                QagsAndMaxPageCount(
                    qags = emptyList(),
                    maxPageCount = 2,
                )
            )
            then(qagInfoRepository).should().getQagsCount()
            then(qagInfoRepository).should().getSupportedQagsPaginated(
                userId = userId,
                thematiqueId = thematiqueId,
                maxDate = dateFreeze,
                offset = 20,
            )
            then(qagInfoRepository).shouldHaveNoMoreInteractions()
            then(thematiqueRepository).should(only()).getThematiqueList()
            then(supportQagRepository).should(only()).getUserSupportedQags(userId = userId)
            then(dateFreezeRepository).should(only()).getQagDateFreeze(userId = userId, thematiqueId = thematiqueId)
            then(mapper).should()
                .toPreview(qag = qag, thematique = thematique, isSupportedByUser = true, isAuthor = false)
        }

    }

}