package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.qagArchive.AnonymizeOldQagUseCase
import fr.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import fr.gouv.agora.usecase.themeHebdo.IsThemeHebdoTransitionUseCase
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
