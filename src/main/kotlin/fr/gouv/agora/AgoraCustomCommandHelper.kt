package fr.gouv.agora

object AgoraCustomCommandHelper {

    private const val CUSTOM_COMMAND_PREFIX = "--run-custom-command="

    private const val CUSTOM_COMMAND_PROPERTY_KEY = "CustomCommand"
    private const val CUSTOM_COMMAND_ARGUMENTS_PROPERTY_KEY = "CustomCommandArguments"
    private const val CUSTOM_COMMAND_ARGUMENTS_SEPARATOR = "\n"

    fun storeCustomCommand(args: Array<String>) {
        args.find { it.startsWith(CUSTOM_COMMAND_PREFIX) }?.let { customCommandFullArgument ->
            val customCommandArgumentIndex = args.indexOf(customCommandFullArgument)

            System.setProperty(
                CUSTOM_COMMAND_PROPERTY_KEY,
                customCommandFullArgument.substringAfter(CUSTOM_COMMAND_PREFIX).trim(),
            )
            args.copyOfRange(customCommandArgumentIndex + 1, args.size)
                .joinToString(separator = CUSTOM_COMMAND_ARGUMENTS_SEPARATOR)
                .takeIf { it.isNotBlank() }
                ?.let { customCommandArguments ->
                    System.setProperty(CUSTOM_COMMAND_ARGUMENTS_PROPERTY_KEY, customCommandArguments)
                }
        }
    }

    fun getStoredCustomCommandAndClear(): AgoraCustomCommand? {
        return System.getProperty(CUSTOM_COMMAND_PROPERTY_KEY)
            ?.takeIf { command -> command.isNotBlank() }
            ?.let { command ->
                AgoraCustomCommand(
                    command = command,
                    arguments = System.getProperty(CUSTOM_COMMAND_ARGUMENTS_PROPERTY_KEY)
                        ?.takeIf { it.isNotBlank() }
                        ?.split(CUSTOM_COMMAND_ARGUMENTS_SEPARATOR)
                        ?.filter { it.isNotBlank() }
                        ?.mapNotNull { argument ->
                            if (argument.startsWith("--") && argument.contains("=")) {
                                argument.substringAfter("--").substringBefore("=") to argument.substringAfter("=")
                            } else null
                        }
                        ?.toMap()

                )
            }
            .also {
                System.clearProperty(CUSTOM_COMMAND_PROPERTY_KEY)
                System.clearProperty(CUSTOM_COMMAND_ARGUMENTS_PROPERTY_KEY)
            }
    }

}

data class AgoraCustomCommand(
    val command: String,
    val arguments: Map<String, String>?,
)