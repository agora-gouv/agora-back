package fr.gouv.agora.usecase.profile

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class AskForDemographicInfoUseCaseTest {

    private lateinit var useCase: AskForDemographicInfoUseCase

    @Mock
    private lateinit var profileRepository: ProfileRepository

    @Mock
    private lateinit var demographicInfoAskDateRepository: DemographicInfoAskDateRepository

    @Mock
    private lateinit var userAnsweredConsultationRepository: UserAnsweredConsultationRepository

    private val profile = Profile(
        gender = Gender.FEMININ,
        yearOfBirth = 1990,
        department = Department.ALLIER_03,
        cityType = CityType.URBAIN,
        jobCategory = JobCategory.OUVRIER,
        voteFrequency = Frequency.JAMAIS,
        publicMeetingFrequency = Frequency.PARFOIS,
        consultationFrequency = Frequency.SOUVENT,
        primaryDepartment = Territoire.Departement.DOUBS,
        secondaryDepartment = Territoire.Departement.NORD,
    )

    private fun buildUseCase(excludedIds: String = "") = AskForDemographicInfoUseCase(
        userAnsweredConsultationRepository = userAnsweredConsultationRepository,
        profileRepository = profileRepository,
        demographicInfoAskDateRepository = demographicInfoAskDateRepository,
        consultationIdsWithoutDemographicAsk = excludedIds,
    )

    @BeforeEach
    fun setUp() {
        useCase = buildUseCase()
    }

    @Nested
    inner class `askForDemographicInfo - when consultationId is in excluded list` {

        @Test
        fun `askForDemographicInfo - when consultationId is in excluded list with single entry - should return false without any repository interaction`() {
            // Given
            useCase = buildUseCase(excludedIds = "excluded-doc-id")

            // When
            val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "excluded-doc-id")

            // Then
            assertThat(result).isEqualTo(false)
            then(profileRepository).shouldHaveNoInteractions()
            then(userAnsweredConsultationRepository).shouldHaveNoInteractions()
            then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `askForDemographicInfo - when consultationId is in excluded list with multiple entries - should return false without any repository interaction`() {
            // Given
            useCase = buildUseCase(excludedIds = "other-id, excluded-doc-id , another-id")

            // When
            val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "excluded-doc-id")

            // Then
            assertThat(result).isEqualTo(false)
            then(profileRepository).shouldHaveNoInteractions()
            then(userAnsweredConsultationRepository).shouldHaveNoInteractions()
            then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `askForDemographicInfo - when consultationId is NOT in excluded list - should follow normal behavior`() {
            // Given
            useCase = buildUseCase(excludedIds = "other-id, another-id")
            given(profileRepository.getProfile(userId = "1234")).willReturn(profile)

            // When
            val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "not-excluded-id")

            // Then
            assertThat(result).isEqualTo(false)
            then(profileRepository).should(only()).getProfile(userId = "1234")
        }

        @Test
        fun `askForDemographicInfo - when excluded list is empty - should follow normal behavior`() {
            // Given
            useCase = buildUseCase(excludedIds = "")
            given(profileRepository.getProfile(userId = "1234")).willReturn(profile)

            // When
            val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "any-consultation-id")

            // Then
            assertThat(result).isEqualTo(false)
            then(profileRepository).should(only()).getProfile(userId = "1234")
        }
    }

    @Test
    fun `askForDemographicInfo - when profile is not null - should return false`() {
        //Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(profile)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "consultId")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(userAnsweredConsultationRepository).shouldHaveNoInteractions()
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null but answered consultation count is lower than 1 - should return false`() {
        // Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234")).willReturn(emptyList())

        // When
        val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "consultId")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(userAnsweredConsultationRepository).should(only()).getAnsweredConsultationIds(userId = "1234")
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null, answered at least 1 consultation and getDate returns null - should return true`() {
        //Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234"))
            .willReturn(listOf("consultationId1"))
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(null)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "consultId")

        // Then
        assertThat(result).isEqualTo(true)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).insertDate(userId = "1234")
    }

    @Test
    fun `askForDemographicInfo - when profile is null, answered at least 1 consultation and getDate returns date previous to (SYSDATE - 30) - should return true`() {
        //Given
        val datePreviousSysDateMinusAskPeriod = LocalDate.now().minusDays(30.toLong() + 1)
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234"))
            .willReturn(listOf("consultationId1"))
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(datePreviousSysDateMinusAskPeriod)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "consultId")

        // Then
        assertThat(result).isEqualTo(true)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).insertDate(userId = "1234")
    }

    @Test
    fun `askForDemographicInfo - when profile is null, answered at least 1 consultation and getDate returns date in ((SYSDATE - 30), SYSDATE) - should return false`() {
        //Given
        val datePreviousSysDateMinusAskPeriod = LocalDate.now().minusDays(30.toLong() / 2)
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234"))
            .willReturn(listOf("consultationId1"))
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(datePreviousSysDateMinusAskPeriod)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234", consultationId = "consultId")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(only()).getDate(userId = "1234")
    }
}
