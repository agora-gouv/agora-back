package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.domain.FicheInventaire
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import fr.gouv.agora.usecase.ficheInventaire.FicheInventaireRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class FicheInventaireRepositoryImpl(
    private val ficheInventaireStrapiRepository: FicheInventaireStrapiRepository,
    private val thematiqueMapper: ThematiqueMapper,
    @Qualifier("shortTermCacheManager")
    private val shortTermCacheManager: CacheManager,
): FicheInventaireRepository {

    companion object {
        const val FICHES_INVENTAIRE_CACHE_NAME = "fichesInventaireCache"
    }

    override fun getAll(filters: FicheInventaireFilters): List<FicheInventaire> {
        val cacheKey = toCacheKey(filters)
        val cached = getFromCache(cacheKey)
        if (cached != null) return cached

        val result = ficheInventaireStrapiRepository.getFichesInventaire(filters).data
            .map { toFicheInventaire(it) }
        putInCache(cacheKey, result)
        return result
    }

    override fun get(id: String): FicheInventaire? {
        val ficheInventaire = ficheInventaireStrapiRepository.getFicheInventaire(id)
            ?: return null
        return toFicheInventaire(ficheInventaire)
    }

    private fun toCacheKey(filters: FicheInventaireFilters): String {
        return listOf(
            "titre=${filters.titre}",
            "thematique=${filters.thematique}",
            "etape=${filters.etape?.sorted()?.joinToString(",")}",
            "condition=${filters.conditionParticipation?.sorted()?.joinToString(",")}",
            "modalite=${filters.modaliteParticipation?.sorted()?.joinToString(",")}",
            "annee=${filters.anneeDeLancement}",
        ).joinToString("|")
    }

    @Suppress("UNCHECKED_CAST")
    private fun getFromCache(cacheKey: String): List<FicheInventaire>? {
        return try {
            shortTermCacheManager.getCache(FICHES_INVENTAIRE_CACHE_NAME)
                ?.get(cacheKey, List::class.java) as? List<FicheInventaire>
        } catch (e: Exception) {
            null
        }
    }

    private fun putInCache(cacheKey: String, fiches: List<FicheInventaire>) {
        try {
            shortTermCacheManager.getCache(FICHES_INVENTAIRE_CACHE_NAME)
                ?.put(cacheKey, fiches)
        } catch (e: Exception) {
            // Ne pas planter si le cache échoue
        }
    }

    private fun toFicheInventaire(fiche: FicheInventaireStrapiDTO): FicheInventaire {
        return FicheInventaire(
            id = fiche.documentId,
            etapeLancement = fiche.etapeLancement.toHtml(),
            etapeAnalyse = fiche.etapeAnalyse.toHtml(),
            etapeSuivi = fiche.etapeSuivi.toHtml(),
            titre = fiche.titre,
            debut = fiche.debut,
            fin = fiche.fin,
            porteur = fiche.porteur,
            lienSite = fiche.lienSite,
            conditionParticipation = fiche.conditionParticipation,
            modaliteParticipation = fiche.modaliteParticipation,
            thematique = thematiqueMapper.toDomain(fiche.thematique),
            illustration = fiche.illustration.mediaUrl(),
            etape = fiche.etape,
            anneeDeLancement = fiche.anneeDeLancement,
            type = fiche.type,
        )
    }
}
