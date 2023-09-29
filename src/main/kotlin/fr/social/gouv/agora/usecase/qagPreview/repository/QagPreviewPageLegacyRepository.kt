package fr.social.gouv.agora.usecase.qagPreview.repository

import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique

@Deprecated("Should not be used anymore")
interface QagPreviewPageLegacyRepository {

    fun getQagPopularList(thematiqueId: String?): List<QagInfoWithSupportAndThematique>?
    fun insertQagPopularList(thematiqueId: String?, qagList: List<QagInfoWithSupportAndThematique>)
    fun evictQagPopularList(thematiqueId: String?)

    fun getQagLatestList(thematiqueId: String?): List<QagInfoWithSupportAndThematique>?
    fun insertQagLatestList(thematiqueId: String?, qagList: List<QagInfoWithSupportAndThematique>)
    fun evictQagLatestList(thematiqueId: String?)

    fun getQagSupportedList(userId: String, thematiqueId: String?): List<QagInfoWithSupportAndThematique>?
    fun insertQagSupportedList(userId: String, thematiqueId: String?, qagList: List<QagInfoWithSupportAndThematique>)
    fun evictQagSupportedList(userId: String, thematiqueId: String?)

}