package cat.content.randomcat.api.cat

import cat.content.randomcat.exeption.impl.CatApiException
import mu.KotlinLogging
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.io.InputStream
import java.util.concurrent.TimeUnit


@Service
class CatApi(
    @Value("\${bot.cat-bot.api.cat.random-cat-gif}") private var gifCatUrl: String,
    @Value("\${bot.cat-bot.api.cat.random-cat}") private var anyCatUrl: String,
    @Value("\${bot.cat-bot.api.cat.max-file-size}") private var maxFileSize: Int
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
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            return client.newCall(request).getResponse(type)
        } catch (e: Exception) {
            e.printStackTrace()
            throw CatApiException("Error while fetching data from cat api ($url)")
        }
    }

    private fun Call.getResponse(type: String): InputStream {
        val response = this.execute()
        if (!response.isSuccessful) {
            logger.warn { "Unexpected code $response" }
            throw IllegalArgumentException()
        }
        val contentType = response.header("Content-Type")
        val contentLength = response.header("Content-Length")?.toLong() ?: 0

        return if (contentType != null && contentType.startsWith(type) && contentLength < maxFileSize * 1024 * 1024) {
            response.body!!.byteStream()
        } else {
            getResponse(type)
        }
    }
}
