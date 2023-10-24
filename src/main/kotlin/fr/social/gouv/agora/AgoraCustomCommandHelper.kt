package fr.social.gouv.agora

object AgoraCustomCommandHelper {

    private const val CUSTOM_COMMAND_PREFIX = "--run-custom-command="

    private const val CUSTOM_COMMAND_PROPERTY_KEY = "CustomCommand"
    private const val CUSTOM_COMMAND_ARGUMENT_PROPERTY_KEY = "CustomCommandArgument"

    fun storeCustomCommand(args: Array<String>) {
        args.find { it.startsWith(CUSTOM_COMMAND_PREFIX) }?.let { customCommandFullArgument ->
            val customCommandArgumentIndex = args.indexOf(customCommandFullArgument)

            System.setProperty(
                CUSTOM_COMMAND_PROPERTY_KEY,
                customCommandFullArgument.substringAfter(CUSTOM_COMMAND_PREFIX).trim(),
            )
            args.getOrNull(customCommandArgumentIndex + 1)?.let { customCommandArgument ->
                System.setProperty(CUSTOM_COMMAND_ARGUMENT_PROPERTY_KEY, customCommandArgument)
            }

        }
    }

    fun getStoredCustomCommandAndClear(): AgoraCustomCommand? {
        return System.getProperty(CUSTOM_COMMAND_PROPERTY_KEY)
            ?.takeIf { command -> command.isNotBlank() }
            ?.let { command ->
                AgoraCustomCommand(
                    command = command,
                    argument = System.getProperty(CUSTOM_COMMAND_ARGUMENT_PROPERTY_KEY)?.takeIf { it.isNotBlank() }
                )
            }
            .also {
                System.clearProperty(CUSTOM_COMMAND_PROPERTY_KEY)
                System.clearProperty(CUSTOM_COMMAND_ARGUMENT_PROPERTY_KEY)
            }
    }

}

data class AgoraCustomCommand(
    val command: String,
    val argument: String?,
)