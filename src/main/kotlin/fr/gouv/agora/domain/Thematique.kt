package fr.gouv.agora.domain

import java.io.Serializable

data class Thematique(
    val id: String,
    val label: String,
    val picto: String,
): Serializable {
    constructor(): this("", "", "")
}
