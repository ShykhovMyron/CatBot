@file:OptIn(DelicateCoroutinesApi::class)

package cat.content.randomcat.command.impl

import cat.content.randomcat.command.CatBotCommand
import cat.content.randomcat.command.TelegramContext
import cat.content.randomcat.command.executeWrapper
import cat.content.randomcat.time.TimeTracker
import kotlinx.coroutines.DelicateCoroutinesApi
import lombok.AllArgsConstructor
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendSticker
import org.telegram.telegrambots.meta.api.methods.stickers.GetStickerSet
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker
import org.telegram.telegrambots.meta.bots.AbsSender


@DelicateCoroutinesApi
@Component
@AllArgsConstructor
class RandomStickerCatCommand(
    @Value("\${bot.cat-bot.command.random-cat-sticker.command}") private val command: String,
    @Value("\${bot.cat-bot.command.random-cat-sticker.description}") private val description: String,
    @Value("#{'\${bot.cat-bot.command.random-cat-sticker.sticker-pucks}'.split(',')}") private val stickerPuckIds: List<String>
) : CatBotCommand(command, description) {
    private val logger = KotlinLogging.logger { }
    private val stickerPuck: MutableSet<Sticker> = mutableSetOf()

    override fun execute(absSender: AbsSender?, user: User?, chat: Chat?, arguments: Array<out String>?) {
        executeWrapper(TelegramContext(absSender, user, chat, arguments), command) { context ->
            val time = TimeTracker()
            val randomSticker = getAllStickers(context.absSender).shuffled().firstOrNull()
            time.timeAndReset()
            absSender?.execute(SendSticker().also {
                it.chatId = chat?.id.toString()
                it.sticker = InputFile(randomSticker?.fileId)
            })
        }
    }

    private fun getAllStickers(absSender: AbsSender?): Set<Sticker> {
        if (stickerPuck.isEmpty()) {
            stickerPuck.addAll(stickerPuckIds.flatMap {
                try {
                    absSender?.execute(GetStickerSet(it))?.stickers ?: emptyList()
                } catch (e: Exception) {
                    logger.warn { "Sticker-puck with id '$it' was not found" }
                    emptyList()
                }
            }.toSet())
        }
        return stickerPuck
    }

}