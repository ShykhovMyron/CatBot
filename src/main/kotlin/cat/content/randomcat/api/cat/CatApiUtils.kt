package cat.content.randomcat.api.cat

import cat.content.randomcat.exeption.impl.CatApiException
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


@Service
class CatApi(
    @Value("\${bot.cat-bot.api.random-cat-gif}") private var gifCatUrl: String,
    @Value("\${bot.cat-bot.api.random-cat}") private var anyCatUrl: String,
    @Value("\${bot.cat-bot.api.max-file-size}") private var maxFileSize: Int
) {
    private val logger = KotlinLogging.logger { }

    suspend fun getGif(): InputFile {
        logger.debug { "Getting random cat gif from $gifCatUrl" }
        return InputFile(getInputStream(gifCatUrl, ""), "cat-gif.gif")
    }

    suspend fun getPhoto(): InputFile {
        logger.debug { "Getting random cat photo from $anyCatUrl" }
        return InputFile(getInputStream(anyCatUrl, "image/"), "cat-photo.jpg")
    }

    @Throws(CatApiException::class)
    private fun getInputStream(url: String, type: String = ""): InputStream {
        try {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connectTimeout = 5000
            urlConnection.readTimeout = 5000
            val contentType = urlConnection.getHeaderField("Content-Type")
            val contentLength = urlConnection.getHeaderField("Content-Length")?.toLong() ?: 0

            return if (contentType != null && contentType.startsWith(type) && contentLength < maxFileSize * 1024 * 1024) {
                urlConnection.inputStream
            } else {
                getInputStream(url, type)
            }
        } catch (e: Exception) {
            throw CatApiException("Error while fetching data from cat api")
        }
    }
}