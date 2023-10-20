package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQag
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class GetModeratusQagListUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val mapper: ModeratusQagMapper,
) {

    fun getQagModeratingList(): List<ModeratusQag> {
        return qagInfoRepository.getQagInfoToModerateList()
            .map { qagInfo -> mapper.toModeratusQag(qagInfo) }
    }

}