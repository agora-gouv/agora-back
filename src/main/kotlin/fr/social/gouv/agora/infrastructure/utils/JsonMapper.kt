package fr.social.gouv.agora.infrastructure.utils

interface JsonMapper<Domain, Json> {
    fun toJson(domain: Domain): Json
}
