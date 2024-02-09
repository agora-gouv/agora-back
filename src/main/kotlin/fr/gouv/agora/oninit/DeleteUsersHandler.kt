package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.deleteUsers.DeleteUsersUseCase
import org.springframework.stereotype.Component

@Component
class DeleteUsersHandler(
    private val deleteUsersUseCase: DeleteUsersUseCase,
) : CustomCommandHandler {

    private companion object {
        private const val USER_IDS_TO_DELETE_ARGUMENT = "userIdsToDelete"
        private const val USER_IDS_TO_DELETE_SEPARATOR = ","
    }

    override fun handleTask(arguments: Map<String, String>?) {
        arguments?.get(USER_IDS_TO_DELETE_ARGUMENT)?.split(USER_IDS_TO_DELETE_SEPARATOR)?.let { userIdsToDelete ->
            println("💁❌ Delete ${userIdsToDelete.size} user(s) with ID:\n${userIdsToDelete.joinToString("\n")}")
            deleteUsersUseCase.deleteUsers(userIdsToDelete)
        }
    }

}