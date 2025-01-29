package fr.gouv.agora.infrastructure.participationCharter.repository

import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.participationCharter.ParticipationCharter
import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ParticipationCharterRepositoryImpl(
    private val participationCharterStrapiRepository: ParticipationCharterStrapiRepository,
    private val clock: Clock,
) : ParticipationCharterRepository {

    override fun getLatestParticipationCharter(): ParticipationCharter {
        val now = LocalDateTime.now(clock)
        val lastParticipationCharter = participationCharterStrapiRepository.getLastParticipationCharter(now).attributes
        val charte = lastParticipationCharter.charte.toHtml()
        val preview = lastParticipationCharter.chartePreview.toHtml()

        return ParticipationCharter(charte, preview)
    }
}
