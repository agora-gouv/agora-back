package fr.gouv.agora.domain

data class ConsultationPreviewPage(
    val ongoingList: List<ConsultationPreview>,
    val finishedList: List<ConsultationPreviewFinished>,
    val answeredList: List<ConsultationPreviewFinished>,
)
