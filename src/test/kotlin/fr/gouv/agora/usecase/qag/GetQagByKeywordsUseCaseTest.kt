package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
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
internal class GetQagByKeywordsUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagByKeywordsUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var supportQagUseCase: SupportQagUseCase

    @MockBean
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
        val thematique = Thematique(
            id = "thematiqueId",
            label = "label",
            picto = "picto",
        )
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
        given(supportQagUseCase.getUserSupportedQagIds("userId")).willReturn(listOf("qagId"))
        val qagInfoWithSupportCount = QagInfoWithSupportCount(
            id = "qagId",
            thematiqueId = "thematiqueId",
            title = "title",
            description = "description",
            date = Date(0),
            status = QagStatus.OPEN,
            username = "username",
            userId = "userId",
            supportCount = 1,
        )
        given(qagInfoRepository.getQagByKeywordsList(listOf("keywords"))).willReturn(listOf(qagInfoWithSupportCount))
        val qagWithSupportCount = QagWithSupportCount(
            qagInfo = qagInfoWithSupportCount,
            thematique = thematique,
        )
        val qagPreview = QagPreview(
            id = "qagId",
            thematique = thematique,
            title = "title",
            username = "username",
            date = Date(0),
            supportCount = 1,
            isSupportedByUser = true,
            isAuthor = true,
        )

        given(
            mapper.toPreview(
                qag = qagWithSupportCount,
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
            qag = qagWithSupportCount,
            isSupportedByUser = true,
            isAuthor = true
        )
    }
}