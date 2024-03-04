package fr.gouv.agora.usecase.profile

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class AskForDemographicInfoUseCaseTest {

    @Autowired
    private lateinit var useCase: AskForDemographicInfoUseCase

    @MockBean
    private lateinit var profileRepository: ProfileRepository

    @MockBean
    private lateinit var demographicInfoAskDateRepository: DemographicInfoAskDateRepository

    @MockBean
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
    )

    @Test
    fun `askForDemographicInfo - when profile is not null - should return false`() {
        //Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(profile)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(userAnsweredConsultationRepository).shouldHaveNoInteractions()
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null but answered consultation count is lower than 2 - should return false`() {
        // Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234")).willReturn(emptyList())

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(userAnsweredConsultationRepository).should(only()).getAnsweredConsultationIds(userId = "1234")
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null, answered at least 2 consultations and getDate returns null - should return true`() {
        //Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234"))
            .willReturn(listOf("consultationId1", "consultationId2"))
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(null)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(true)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).insertDate(userId = "1234")
    }

    @Test
    fun `askForDemographicInfo - when profile is null, answered at least 2 consultations and getDate returns date previous to (SYSDATE - 30) - should return true`() {
        //Given
        val datePreviousSysDateMinusAskPeriod = LocalDate.now().minusDays(30.toLong() + 1)
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234"))
            .willReturn(listOf("consultationId1", "consultationId2"))
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(datePreviousSysDateMinusAskPeriod)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(true)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).insertDate(userId = "1234")
    }

    @Test
    fun `askForDemographicInfo - when profile is null, answered at least 2 consultations and getDate returns date in ((SYSDATE - 30), SYSDATE) - should return false`() {
        //Given
        val datePreviousSysDateMinusAskPeriod = LocalDate.now().minusDays(30.toLong() / 2)
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(userAnsweredConsultationRepository.getAnsweredConsultationIds(userId = "1234"))
            .willReturn(listOf("consultationId1", "consultationId2"))
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(datePreviousSysDateMinusAskPeriod)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(only()).getDate(userId = "1234")
    }
}