package fr.social.gouv.agora.infrastructure.utils

interface Mapper<Domain, DTO> {
    fun toDomain(dto: DTO): Domain
    fun toDto(domain: Domain): DTO
}
