package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.qagArchive.AnonymizeOldQagUseCase
import fr.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import fr.gouv.agora.usecase.themeHebdo.IsThemeHebdoTransitionUseCase
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class WeeklyTasksHandlerTest {

    @InjectMocks
    private lateinit var handler: WeeklyTasksHandler

    @Mock
    private lateinit var isThemeHebdoTransitionUseCase: IsThemeHebdoTransitionUseCase

    @Mock
    private lateinit var selectMostPopularQagUseCase: SelectMostPopularQagUseCase

    @Mock
    private lateinit var archiveOldQagUseCase: ArchiveOldQagUseCase

    @Mock
    private lateinit var anonymizeOldQagUseCase: AnonymizeOldQagUseCase

    @Nested
    inner class WithoutIgnoreTransition {

        @Test
        fun `handleTask - when in theme transition - should select most popular qag, archive old qags and anonymize old qags`() {
            // Given
            given(isThemeHebdoTransitionUseCase.isInTransition()).willReturn(true)

            // When
            handler.handleTask(null)

            // Then
            then(isThemeHebdoTransitionUseCase).should().isInTransition()
            then(selectMostPopularQagUseCase).should().putMostPopularQagInSelectedStatus()
            then(archiveOldQagUseCase).should().archiveOldQag()
            then(anonymizeOldQagUseCase).should().anonymizeOldQag()
            then(selectMostPopularQagUseCase).shouldHaveNoMoreInteractions()
            then(archiveOldQagUseCase).shouldHaveNoMoreInteractions()
            then(anonymizeOldQagUseCase).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `handleTask - when not in theme transition - should only anonymize old qags`() {
            // Given
            given(isThemeHebdoTransitionUseCase.isInTransition()).willReturn(false)

            // When
            handler.handleTask(null)

            // Then
            then(isThemeHebdoTransitionUseCase).should().isInTransition()
            then(selectMostPopularQagUseCase).shouldHaveNoInteractions()
            then(archiveOldQagUseCase).shouldHaveNoInteractions()
            then(anonymizeOldQagUseCase).should().anonymizeOldQag()
            then(anonymizeOldQagUseCase).shouldHaveNoMoreInteractions()
        }
    }

    @Nested
    inner class WithIgnoreTransition {

        @Test
        fun `handleTask - when ignoreTransition is true - should always select most popular qag, archive old qags and anonymize old qags without checking transition`() {
            // Given

            // When
            handler.handleTask(mapOf("force_question_selection" to "true"))

            // Then
            then(isThemeHebdoTransitionUseCase).shouldHaveNoInteractions()
            then(selectMostPopularQagUseCase).should().putMostPopularQagInSelectedStatus()
            then(archiveOldQagUseCase).should().archiveOldQag()
            then(anonymizeOldQagUseCase).should().anonymizeOldQag()
            then(selectMostPopularQagUseCase).shouldHaveNoMoreInteractions()
            then(archiveOldQagUseCase).shouldHaveNoMoreInteractions()
            then(anonymizeOldQagUseCase).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `handleTask - when ignoreTransition is false - should check transition and only anonymize old qags when not in transition`() {
            // Given
            given(isThemeHebdoTransitionUseCase.isInTransition()).willReturn(false)

            // When
            handler.handleTask(mapOf("force_question_selection" to "false"))

            // Then
            then(isThemeHebdoTransitionUseCase).should().isInTransition()
            then(selectMostPopularQagUseCase).shouldHaveNoInteractions()
            then(archiveOldQagUseCase).shouldHaveNoInteractions()
            then(anonymizeOldQagUseCase).should().anonymizeOldQag()
            then(anonymizeOldQagUseCase).shouldHaveNoMoreInteractions()
        }
    }
}
