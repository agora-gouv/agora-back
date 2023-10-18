package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.QagSelectedForResponse
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
internal class GetQagSelectedForResponseUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagSelectedForResponseUseCase

    @MockBean
    private lateinit var filterGenerator: QagSelectForResponseFilterGenerator

    @MockBean
    private lateinit var mapper: QagSelectedForResponseMapper

//    @Test
//    fun `getQagSelectedForResponseList - when has no qag - should return emptyList`() {
//        // Given
//        val qagFilters = mock(QagFilters::class.java)
//        given(filterGenerator.getFilter()).willReturn(qagFilters)
//        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(emptyList())
//
//        // When
//        val result = useCase.getQagSelectedForResponseList(userId = "userId")
//
//        // Then
//        assertThat(result).isEqualTo(emptyList<QagSelectedForResponse>())
//        then(filterGenerator).should(only()).getFilter()
//        then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters)
//        then(mapper).shouldHaveNoInteractions()
//    }
//
//    @Test
//    fun `getQagSelectedForResponseList - when has qags - should return mapped qags`() {
//        // Given
//        val qagFilters = mock(QagFilters::class.java)
//        given(filterGenerator.getFilter()).willReturn(qagFilters)
//
//        val qag = mock(QagInfoWithSupportAndThematique::class.java)
//        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(listOf(qag))
//
//        val qagSelectedForResponse = mock(QagSelectedForResponse::class.java)
//        given(mapper.toQagSelectedForResponse(qag = qag, userId = "userId")).willReturn(qagSelectedForResponse)
//
//        // When
//        val result = useCase.getQagSelectedForResponseList(userId = "userId")
//
//        // Then
//        assertThat(result).isEqualTo(listOf(qagSelectedForResponse))
//        then(filterGenerator).should(only()).getFilter()
//        then(getQagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters)
//        then(mapper).should(only()).toQagSelectedForResponse(qag = qag, userId = "userId")
//    }

}