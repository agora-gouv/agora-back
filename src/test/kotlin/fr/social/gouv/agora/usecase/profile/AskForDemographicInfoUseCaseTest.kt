package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.profile.repository.DemographicInfoAskDateRepository
import fr.social.gouv.agora.usecase.profile.repository.ProfileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class AskForDemographicInfoUseCaseTest {

    @Autowired
    private lateinit var useCase: AskForDemographicInfoUseCase

    @MockBean
    private lateinit var profileRepository: ProfileRepository

    @MockBean
    private lateinit var demographicInfoAskDateRepository: DemographicInfoAskDateRepository

    private val profile = Profile(
        gender = Gender.FEMININ,
        yearOfBirth = 1990,
        department = Department.ALLIER_3,
        cityType = CityType.URBAIN,
        jobCategory = JobCategory.OUVRIER,
        voteFrequency = Frequency.JAMAIS,
        publicMeetingFrequency = Frequency.PARFOIS,
        consultationFrequency = Frequency.SOUVENT,
    )

    companion object {
        private const val DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO = 30
    }

    @Test
    fun `askForDemographicInfo - when userId is not null and profile is not null - should return false`() {
        //Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(profile)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null and getDate returns null - should return true`() {
        //Given
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
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
    fun `askForDemographicInfo - when profile is null and getDate returns date previous to (SYSDATE - DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO) - should return true`() {
        //Given
        val datePreviousSysDateMinusAskPeriod =
            LocalDate.now().minusDays(DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO.toLong() + 1)
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(datePreviousSysDateMinusAskPeriod)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(true)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = "1234")
        then(demographicInfoAskDateRepository).should(times(1)).updateDate(userId = "1234")
    }

    @Test
    fun `askForDemographicInfo - when profile is null and getDate returns date in ((SYSDATE - DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO), SYSDATE) - should return false`() {
        //Given
        val datePreviousSysDateMinusAskPeriod =
            LocalDate.now().minusDays(DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO.toLong() / 2)
        given(profileRepository.getProfile(userId = "1234")).willReturn(null)
        given(demographicInfoAskDateRepository.getDate(userId = "1234")).willReturn(datePreviousSysDateMinusAskPeriod)

        // When
        val result = useCase.askForDemographicInfo(userId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(profileRepository).should(only()).getProfile(userId = "1234")
        then(demographicInfoAskDateRepository).should(only()).getDate(userId = "1234")
    }
}