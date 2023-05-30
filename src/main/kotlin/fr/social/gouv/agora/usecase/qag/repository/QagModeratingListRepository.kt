package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingListRepository {
    fun getQagModeratingList(): List<QagModeratingInfo>
    fun getModeratingQagCount(): Int
}