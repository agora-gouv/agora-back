package fr.gouv.agora.usecase.errorMessages.repository

interface ErrorMessagesRepository {
    fun getQagDisabledErrorMessage(): String
    fun getQagErrorMessageOneByWeek(): String
}