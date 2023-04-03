package cat.content.randomcat.command

import cat.content.randomcat.exeption.CustomException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

private val logger = KotlinLogging.logger { }

@DelicateCoroutinesApi
fun executeWrapper(
    context: TelegramContext,
    executeFunction: suspend (TelegramContext) -> Unit
) {
    GlobalScope.launch {
        val chatId = context.chat?.id.toString()
        try {
            executeFunction(context)
        } catch (e: TelegramApiRequestException) {
            logger.warn { "Telegram API error\n\t${e.apiResponse}" }
            context.absSender?.executeAsync(SendMessage(chatId, e.apiResponse))
        } catch (e: CustomException) {
            logger.warn { "Error\n${e.message}" }
            context.absSender?.executeAsync(SendMessage(chatId, e.message ?: "Unknown error"))
        } catch (e: Exception) {
            logger.warn { "Error\n${e.localizedMessage}" }
            context.absSender?.executeAsync(SendMessage(chatId, "Something went wrong \uD83D\uDE3F"))
        }
    }
}

data class TelegramContext(
    val absSender: AbsSender?,
    val user: User?,
    val chat: Chat?,
    val arguments: Array<out String>?
)
