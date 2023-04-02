package cat.content.randomcat.command

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand

abstract class CatBotCommand(command: String, description: String) : BotCommand(command, description)