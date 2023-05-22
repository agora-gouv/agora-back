package fr.social.gouv.agora.usecase.profile

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
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

    @MockBean
    private lateinit var loginRepository: LoginRepository


    private val profile = Profile(
        gender = Gender.FEMININ_F,
        yearOfBirth = 1990,
        department = Department.ALLIER_3,
        cityType = CityType.URBAIN_U,
        jobCategory = JobCategory.OUVRIER_OU,
        voteFrequency = Frequency.JAMAIS_J,
        publicMeetingFrequency = Frequency.PARFOIS_P,
        consultationFrequency = Frequency.SOUVENT_S
    )

    private val userUUID = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120003")

    companion object {
        private const val DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO = 30
    }

    @Test
    fun `askForDemographicInfo - when userId is null - should return false`() {
        //Given
        given(loginRepository.getUser(deviceId = "1234")).willReturn(null)

        // When
        val result = useCase.askForDemographicInfo(deviceId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(loginRepository).should(only()).getUser(deviceId = "1234")
        then(profileRepository).shouldHaveNoInteractions()
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when userId is not null and profile is not null - should return false`() {
        //Given
        given(loginRepository.getUser(deviceId = "1234")).willReturn(UserInfo(userUUID.toString()))
        given(profileRepository.getProfile(userId = userUUID.toString())).willReturn(profile)

        // When
        val result = useCase.askForDemographicInfo(deviceId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(loginRepository).should(only()).getUser(deviceId = "1234")
        then(profileRepository).should(only()).getProfile(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null and getDate returns null - should return true`() {
        //Given
        given(loginRepository.getUser(deviceId = "1234")).willReturn(UserInfo(userUUID.toString()))
        given(profileRepository.getProfile(userId = userUUID.toString())).willReturn(null)
        given(demographicInfoAskDateRepository.getDate(userUUID.toString())).willReturn(null)

        // When
        val result = useCase.askForDemographicInfo(deviceId = "1234")

        // Then
        assertThat(result).isEqualTo(true)
        then(loginRepository).should(only()).getUser(deviceId = "1234")
        then(profileRepository).should(only()).getProfile(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).should(times(1)).insertDate(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null and getDate returns date before to (SYSDATE - DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO) - should return true`() {
        //Given
        val datePreviousSysDateMinusAskPeriod =
            LocalDate.now().minusDays(DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO.toLong() + 1)
        given(loginRepository.getUser(deviceId = "1234")).willReturn(UserInfo(userUUID.toString()))
        given(profileRepository.getProfile(userId = userUUID.toString())).willReturn(null)
        given(demographicInfoAskDateRepository.getDate(userUUID.toString())).willReturn(
            datePreviousSysDateMinusAskPeriod
        )

        // When
        val result = useCase.askForDemographicInfo(deviceId = "1234")

        // Then
        assertThat(result).isEqualTo(true)
        then(loginRepository).should(only()).getUser(deviceId = "1234")
        then(profileRepository).should(only()).getProfile(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).should(times(1)).getDate(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).should(times(1)).updateDate(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `askForDemographicInfo - when profile is null and getDate returns date in ((SYSDATE - DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO), SYSDATE) - should return false`() {
        //Given
        val datePreviousSysDateMinusAskPeriod =
            LocalDate.now().minusDays(DAYS_BEFORE_ASKING_DEMOGRAPHIC_INFO.toLong() / 2)
        given(loginRepository.getUser(deviceId = "1234")).willReturn(UserInfo(userUUID.toString()))
        given(profileRepository.getProfile(userId = userUUID.toString())).willReturn(null)
        given(demographicInfoAskDateRepository.getDate(userUUID.toString())).willReturn(
            datePreviousSysDateMinusAskPeriod
        )

        // When
        val result = useCase.askForDemographicInfo(deviceId = "1234")

        // Then
        assertThat(result).isEqualTo(false)
        then(loginRepository).should(only()).getUser(deviceId = "1234")
        then(profileRepository).should(only()).getProfile(userId = userUUID.toString())
        then(demographicInfoAskDateRepository).should(only()).getDate(userId = userUUID.toString())
    }
}