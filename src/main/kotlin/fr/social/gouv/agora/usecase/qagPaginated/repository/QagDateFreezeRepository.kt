package fr.social.gouv.agora.usecase.qagPaginated.repository

import java.util.*

interface QagDateFreezeRepository {
    fun initQagDateFreeze(userId: String, thematiqueId: String?): Date
    fun getQagDateFreeze(userId: String, thematiqueId: String?): Date
}