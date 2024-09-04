package fr.gouv.agora.usecase.participationCharter.repository

import fr.gouv.agora.usecase.participationCharter.ParticipationCharter

interface ParticipationCharterRepository {
    fun getLatestParticipationCharter(): ParticipationCharter
}
