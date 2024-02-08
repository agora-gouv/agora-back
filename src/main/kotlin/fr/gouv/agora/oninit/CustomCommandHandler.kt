package fr.gouv.agora.oninit

interface CustomCommandHandler {
    fun handleTask(arguments: Map<String, String>?)
}