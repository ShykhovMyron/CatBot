@file:OptIn(DelicateCoroutinesApi::class)

package cat.content.randomcat.command.impl

import cat.content.randomcat.api.cat.CatApi
import cat.content.randomcat.command.CatBotCommand
import cat.content.randomcat.command.TelegramContext
import cat.content.randomcat.command.executeWrapper
import kotlinx.coroutines.DelicateCoroutinesApi
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender


@DelicateCoroutinesApi
@Component
@AllArgsConstructor
class RandomPhotoCatCommand(
    val catApi: CatApi,
    @Value("\${bot.cat-bot.command.random-cat-photo.command}") private val command: String,
    @Value("\${bot.cat-bot.command.random-cat-photo.description}") private val description: String
) : CatBotCommand(command, description) {

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        executeWrapper(TelegramContext(absSender, user, chat, arguments), command) { context ->
            val photo = catApi.getPhoto()
            val sendAnimation = SendPhoto(context.chat?.id.toString(), photo)
            absSender?.execute(sendAnimation)
        }
    }
}