package fr.gouv.agora.oninit

import fr.gouv.agora.AgoraCustomCommandHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class AgoraCustomCommandHandler(
    private val applicationContext: ApplicationContext,
    private val dailyTasksHandler: DailyTasksHandler,
    private val weeklyTasksHandler: WeeklyTasksHandler,
) : InitializingBean {
    private val logger: Logger = LoggerFactory.getLogger(AgoraCustomCommandHandler::class.java)

    companion object {
        private const val DAILY_TASKS = "dailyTasks"
        private const val WEEKLY_TASKS = "weeklyTasks"
    }

    override fun afterPropertiesSet() {
        if (handleCustomCommand()) {
            SpringApplication.exit(applicationContext, { 0 })
            logger.info("⚙️ Run custom command finished")
        }
    }

    private fun handleCustomCommand(): Boolean {
        return AgoraCustomCommandHelper.getStoredCustomCommandAndClear()?.let { customCommand ->
            logger.info("⚙️ Run custom command = ${customCommand.command} / argument = ${customCommand.arguments}")
            getHandler(customCommand.command)?.handleTask(customCommand.arguments)
            true
        } ?: false
    }

    private fun getHandler(command: String): CustomCommandHandler? {
        return when (command) {
            DAILY_TASKS -> dailyTasksHandler
            WEEKLY_TASKS -> weeklyTasksHandler
            else -> null
        }
    }

}
