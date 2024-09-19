package fr.gouv.agora.domain.exceptions

class InvalidTerritoryException(territoire: String): Exception("Le territoire $territoire n'existe pas.")
