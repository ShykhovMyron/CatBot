package cat.content.randomcat.command.impl

import cat.content.randomcat.api.cat.CatApi
import cat.content.randomcat.command.CatBotCommand
import lombok.AllArgsConstructor
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


@Component
@AllArgsConstructor
class RandomGifCatCommand(
    val catApi: CatApi,
    @Value("\${bot.cat-bot.command.random-cat-gif.command}") private val command: String,
    @Value("\${bot.cat-bot.command.random-cat-gif.description}") private val description: String
) : CatBotCommand(command, description) {
    private val logger = KotlinLogging.logger { }

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        val chatId = chat?.id.toString()
        try {
            val gif=catApi.getGif()
            val sendAnimation = SendAnimation(chatId, InputFile(gif, "cat-gif.gif"))
            logger.debug { "Sending random cat gif via /$command command" }
            absSender?.execute(sendAnimation)
        } catch (e: TelegramApiException) {
            logger.debug { "Telegram API error while sending random cat gif\n${e.localizedMessage}" }
        } catch (e: Exception) {
            logger.debug { "Error while sending random cat gif\n${e.localizedMessage}" }
        }
    }
}