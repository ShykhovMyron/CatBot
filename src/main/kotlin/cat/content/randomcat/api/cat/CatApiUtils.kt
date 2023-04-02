package cat.content.randomcat.api.cat

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.URL


@Service
class CatApi(
    @Value("\${bot.cat-bot.api.random-cat-gif}") private var gifUrl: String
) {
    private val logger = KotlinLogging.logger { }

    fun getGif(): InputStream? {
        logger.debug { "Getting random cat gif from $gifUrl" }
        val gif = URL(gifUrl).openStream()
        return gif
    }
}