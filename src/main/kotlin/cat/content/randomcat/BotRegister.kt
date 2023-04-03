package cat.content.randomcat

import cat.content.randomcat.bot.TelegramBot
import lombok.AllArgsConstructor
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
@AllArgsConstructor
class BotRegister(val bots: List<TelegramBot>) : CommandLineRunner {
    private val logger = KotlinLogging.logger { }
    override fun run(vararg args: String?) {
        try {
            val telegramBotApi = TelegramBotsApi(DefaultBotSession::class.java)
            bots.map { if (it is LongPollingBot) telegramBotApi.registerBot(it) }
        } catch (e: Exception) {
            logger.error { "Error while bots registration\n${e.printStackTrace()}" }
        }
    }
}