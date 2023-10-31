package fr.gouv.agora.domain

data class ModeratusQagLockResult(
    val qagId: String,
    val lockResult: QagLockResult,
)

enum class QagLockResult {
    SUCCESS,
    NOT_FOUND,
}