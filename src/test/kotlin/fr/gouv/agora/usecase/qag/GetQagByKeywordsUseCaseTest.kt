package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GetQagByKeywordsUseCaseTest {

    @InjectMocks
    private lateinit var useCase: GetQagByKeywordsUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var thematiqueRepository: ThematiqueRepository

    @Mock
    private lateinit var supportQagUseCase: SupportQagUseCase

    @Mock
    private lateinit var mapper: QagPreviewMapper

    @Test
    fun `getQagByKeywordsUseCase - when database returns emptyList() - should return emptyList`() {
        // Given
        given(qagInfoRepository.getQagByKeywordsList(listOf("keywords"))).willReturn(emptyList())

        // When
        val result = useCase.getQagByKeywordsUseCase(userId = "userId", keywords = listOf("keywords"))

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getQagByKeywordsList(listOf("keywords"))
        then(supportQagUseCase).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagByKeywordsUseCase - when thematique doesn't exist - should return emptyList`() {
        // Given
        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
            given(it.label).willReturn("label")
            given(it.picto).willReturn("picto")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
        val qagInfoWithSupportCount =
            mock(QagInfoWithSupportCount::class.java).also { given(it.thematiqueId).willReturn("thematiqueId2") }
        given(qagInfoRepository.getQagByKeywordsList(listOf("keywords"))).willReturn(listOf(qagInfoWithSupportCount))

        // When
        val result = useCase.getQagByKeywordsUseCase(userId = "userId", keywords = listOf("keywords"))

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getQagByKeywordsList(listOf("keywords"))
        then(thematiqueRepository).should(only()).getThematiqueList()
        then(supportQagUseCase).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagByKeywordsUseCase - when thematique and qag exist - should return mapped qag`() {
        // Given
        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
        given(supportQagUseCase.getUserSupportedQagIds("userId")).willReturn(listOf("qagId"))
        val qagInfoWithSupportCount =
            mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.userId).willReturn("userId")
            }
        given(qagInfoRepository.getQagByKeywordsList(listOf("keywords"))).willReturn(listOf(qagInfoWithSupportCount))
        val qagPreview = mock(QagPreview::class.java)
        given(
            mapper.toPreview(
                qag = qagInfoWithSupportCount,
                thematique = thematique,
                isSupportedByUser = true,
                isAuthor = true
            )
        ).willReturn(qagPreview)

        // When
        val result = useCase.getQagByKeywordsUseCase(userId = "userId", keywords = listOf("keywords"))

        // Then
        assertThat(result).isEqualTo(listOf(qagPreview))
        then(qagInfoRepository).should(only()).getQagByKeywordsList(listOf("keywords"))
        then(thematiqueRepository).should(only()).getThematiqueList()
        then(supportQagUseCase).should(only()).getUserSupportedQagIds("userId")
        then(mapper).should(only()).toPreview(
            qag = qagInfoWithSupportCount,
            thematique = thematique,
            isSupportedByUser = true,
            isAuthor = true
        )
    }
}
