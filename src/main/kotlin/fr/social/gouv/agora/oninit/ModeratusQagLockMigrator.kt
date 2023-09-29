package fr.social.gouv.agora.oninit

import fr.social.gouv.agora.infrastructure.moderatus.repository.ModeratusQagLockCacheRepositoryLegacy
import fr.social.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ModeratusQagLockMigrator(
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
    private val legacyRepository: ModeratusQagLockCacheRepositoryLegacy,
) : InitializingBean {

    @Suppress("deprecation")
    override fun afterPropertiesSet() {
        println("📤 Migrating Moderatus data...")
        val lockedQagIds = legacyRepository.getLockedQagIds()
        if (lockedQagIds.isNotEmpty()) {
            legacyRepository.clear()
            moderatusQagLockRepository.addLockedIds(lockedQagIds)
            println("📤 Migrating Moderatus data finished")
        } else {
            println("📤 Migrating Moderatus data unnecessary")
        }
    }

}