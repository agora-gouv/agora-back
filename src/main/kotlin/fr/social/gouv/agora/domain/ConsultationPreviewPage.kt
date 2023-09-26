package fr.social.gouv.agora.domain

data class ConsultationPreviewPage(
    val ongoingList: List<ConsultationPreviewOngoing>,
    val finishedList: List<ConsultationPreviewFinished>,
    val answeredList: List<ConsultationPreviewAnswered>,
)