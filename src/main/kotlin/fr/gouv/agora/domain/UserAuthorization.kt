package fr.gouv.agora.domain

enum class UserAuthorization {
    VIEW_CONSULTATION,
    VIEW_UNPUBLISHED_CONSULTATION,
    ANSWER_CONSULTATION,
    VIEW_QAG,
    SUPPORT_QAG,
    FEEDBACK_QAG_RESPONSE,
    ADD_QAG,
    MODERATE_QAG,
    ADMIN_APIS,
    ;

    companion object {
        fun getUserAuthorizations() = listOf(
            VIEW_CONSULTATION,
            ANSWER_CONSULTATION,
            VIEW_QAG,
            SUPPORT_QAG,
            FEEDBACK_QAG_RESPONSE,
            ADD_QAG,
        )

        fun getPublisherAuthorizations() = listOf(
            *getUserAuthorizations().toTypedArray(),
            VIEW_UNPUBLISHED_CONSULTATION
        )

        fun getModeratorAuthorizations() = listOf(
            *getUserAuthorizations().toTypedArray(),
            MODERATE_QAG,
        )

        fun geAdminAuthorizations() = values().toList()
    }

}
