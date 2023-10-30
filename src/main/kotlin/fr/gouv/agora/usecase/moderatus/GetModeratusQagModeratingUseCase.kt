package fr.gouv.agora.usecase.moderatus

import fr.gouv.agora.domain.ModeratusQag
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
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