package cat.content.randomcat

import cat.content.randomcat.bot.TelegramBot
import lombok.AllArgsConstructor
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
@AllArgsConstructor
class BotRegister(val bots: List<TelegramBot>) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val telegramBotApi = TelegramBotsApi(DefaultBotSession::class.java)
        bots.map { if (it is LongPollingBot) telegramBotApi.registerBot(it) }
    }
}