package cat.content.randomcat.api.cat

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.net.URL


@Service
class CatApi(
    @Value("\${bot.cat-bot.api.random-cat-gif}") private var gifUrl: String
) {
    private val logger = KotlinLogging.logger { }

    suspend fun getGif(): InputFile {
        logger.debug { "Getting random cat gif from $gifUrl" }
        return InputFile(URL(gifUrl).openStream(), "cat-gif.gif")
    }
}