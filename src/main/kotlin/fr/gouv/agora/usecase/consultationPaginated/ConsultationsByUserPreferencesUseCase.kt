package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Territoire
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

        val territoires = Territoire.of(userProfile)

        val thematiques = thematiqueRepository.getThematiqueList()
        val consultations =
            consultationPreviewFinishedRepository.getConsultationFinishedList(territoires)
                .let { consultationsInfo -> mapper.toConsultationPreviewFinished(consultationsInfo, thematiques) }

        return consultations
    }
}
