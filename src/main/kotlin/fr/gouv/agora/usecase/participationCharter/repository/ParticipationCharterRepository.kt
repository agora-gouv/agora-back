package fr.gouv.agora.usecase.participationCharter.repository

interface ParticipationCharterRepository {
    fun getLatestParticipationCharter(): String
}