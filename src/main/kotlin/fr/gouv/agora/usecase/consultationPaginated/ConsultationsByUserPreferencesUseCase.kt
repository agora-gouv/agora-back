package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.domain.Territoire.Region.*
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ConsultationsByUserPreferencesUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
    private val profileRepository: ProfileRepository,
    private val authentificationHelper: AuthentificationHelper,
) {
    fun execute(): List<ConsultationPreviewFinished> {
        val userId = authentificationHelper.getUserId()!!
        val userProfile = profileRepository.getProfile(userId)

        val primaryDepartment = userProfile?.primaryDepartment
        val secondaryDepartment = userProfile?.secondaryDepartment
        val primaryRegion = Territoire.Region.getByDepartment(primaryDepartment)
        val secondaryRegion = Territoire.Region.getByDepartment(secondaryDepartment)

        val territories = listOfNotNull(
            Territoire.Pays.FRANCE, primaryDepartment, secondaryDepartment,
            primaryRegion, secondaryRegion
        ).distinct()

        val thematiques = thematiqueRepository.getThematiqueList()
        val consultations =
            consultationPreviewFinishedRepository.getConsultationFinishedList(territories)
                .let { consultationsInfo -> mapper.toConsultationPreviewFinished(consultationsInfo, thematiques) }

        return consultations
    }
}

data class ConsultationFinishedPaginatedList(
    val consultationFinishedList: List<ConsultationPreviewFinished>,
    val maxPageNumber: Int,
)
