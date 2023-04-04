package cat.content.randomcat.bot.impl

import cat.content.randomcat.bot.TelegramBot
import cat.content.randomcat.command.CatBotCommand
import lombok.AllArgsConstructor
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
@AllArgsConstructor
class CatBot(
    commands: List<CatBotCommand>,
    @Value("\${bot.cat-bot.username}") private var username: String,
    @Value("\${bot.cat-bot.token}") private var token: String
) : TelegramBot, TelegramLongPollingCommandBot() {
    private val logger = KotlinLogging.logger { }

    init {
        for (command in commands) {
            register(command)
        }
        logger.info { "Bot with username '$username' loaded successfully" }
    }

    override fun getBotUsername(): String {
        return username
    }

    override fun processNonCommandUpdate(update: Update?) {
        //disable
    }

    @Deprecated("Deprecated in Java")
    override fun getBotToken(): String {
        return token
    }
}