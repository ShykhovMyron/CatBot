package cat.content.randomcat.utils

import cat.content.randomcat.exeption.CustomException
import mu.KotlinLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

object ApiRequestHandler {
    private val logger = KotlinLogging.logger { }
    private val restTemplate = RestTemplate()

    @Throws(CustomException::class)
    fun <T> parse(
        responseType: ParameterizedTypeReference<T>,
        url: String,
        params: Map<String, String> = emptyMap(),
        method: HttpMethod
    ): T? {
        logger.debug {
            """
            | Fetching data from
            |  url: ${method.name()} - ${url + getParamsString(params)}
            |  responseType: ${responseType.type.typeName}
        """.trimMargin()
        }

        val response: ResponseEntity<T> = restTemplate.exchange(
            url + getParamsString(params),
            method,
            null,
            responseType
        )

        if (!response.statusCode.is2xxSuccessful) throw CustomException(
            "Error while fetching data from ${
                url + getParamsString(
                    params
                )
            }"
        )

        return response.body
    }

    fun getParamsString(params: Map<String, String>): String {
        return if (params.isEmpty()) {
            ""
        } else {
            "?" + params.map { "${it.key}=${it.value}" }.joinToString("&")
        }
    }
}