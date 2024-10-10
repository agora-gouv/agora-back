package fr.gouv.agora.infrastructure.referentiel

import fr.gouv.agora.domain.Territoire
import org.springframework.stereotype.Component

@Component
class TerritoiresJsonMapper {
    fun toJson(regions: Array<Territoire.Region>, pays: Array<Territoire.Pays>): TerritoiresJson {
        val regionsEtDepartements = regions.map { region ->
            val departements = region.departements.map { departement ->
                DepartementJson(departement.value, departement.codePostal)
            }

            RegionJson(region.value, departements)
        }

        return TerritoiresJson(regionsEtDepartements, Territoire.Pays.values().map { it.value })
    }
}
