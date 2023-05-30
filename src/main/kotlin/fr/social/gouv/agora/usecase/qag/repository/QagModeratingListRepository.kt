package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingListRepository {
    fun getQagModeratingList(): List<QagModeratingInfo>
}