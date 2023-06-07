package fr.social.gouv.agora.usecase.errorMessages.repository

interface ErrorMessagesRepository {
    fun getQagDisabledErrorMessage(): String
    fun getQagErrorMessageOneByWeek(): String
    fun getQagModeratedNotificationMessage(): String
}