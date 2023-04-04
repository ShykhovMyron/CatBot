package cat.content.randomcat.command

import cat.content.randomcat.exeption.CustomException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lombok.EqualsAndHashCode
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
    command: String,
    executeFunction: suspend (TelegramContext) -> Unit
) {
    if (context.chat != null && !checkChat(context.chat.id, command)) {
        logger.warn { "User ${context.chat.firstName} get messages limit per minute for command '/$command'" }
        context.absSender?.executeAsync(
            SendMessage(
                context.chat.id.toString(), "You get messages limit per minute for /$command\n" +
                        "Time left: ${checkTimeLeft(context.chat.id, command)} sec"
            )
        )
        return
    }
    GlobalScope.launch {
        logger.debug { "User '${context.chat?.firstName ?: ""}', command '/$command'" }
        val chatId = context.chat?.id.toString()
        try {
            executeFunction(context)
        } catch (e: TelegramApiRequestException) {
            logger.warn { "Telegram API error\n\t${e.apiResponse}" }
            context.absSender?.executeAsync(
                SendMessage(
                    chatId,
                    "Telegram API error: " + e.apiResponse + " sec\nTry another command(s)"
                )
            )
        } catch (e: CustomException) {
            logger.warn { "Error\n${e.message}" }
            context.absSender?.executeAsync(SendMessage(chatId, e.message ?: "Unknown error"))
        } catch (e: Exception) {
            logger.warn { "Error\n${e.localizedMessage}" }
            context.absSender?.executeAsync(SendMessage(chatId, "Something went wrong \uD83D\uDE3F"))
        }
    }
}

@EqualsAndHashCode
data class TelegramContext(
    val absSender: AbsSender?,
    val user: User?,
    val chat: Chat?,
    val arguments: Array<out String>?
)
