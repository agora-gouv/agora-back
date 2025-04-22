package fr.gouv.agora.usecase.ficheInventaire

class FicheInventaireNotFound(ficheInventaireId: String) : Exception("Fiche inventaire with id '$ficheInventaireId' was not found")
