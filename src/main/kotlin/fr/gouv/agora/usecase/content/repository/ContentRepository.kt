package fr.gouv.agora.usecase.content.repository

interface ContentRepository {
    fun getPagePoserMaQuestion(): String
    fun getPageQuestionsAuGouvernement(): String
    fun getPageReponseAuxQuestionsAuGouvernement(): String
}
