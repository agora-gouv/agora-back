package fr.social.gouv.agora.usecase.qag.repository

interface QagModeratingLockRepository {
    fun isQagLocked(qagId: String): Boolean
    fun setQagLocked(qagId: String)
}

enregistre liste de qag lockées
        flitrer pour ne pas renvoyer les qag lockéespris
        dans le filtre on ne prend pas en compte les lock expiré
        dans cache couple qagId/date  date /list