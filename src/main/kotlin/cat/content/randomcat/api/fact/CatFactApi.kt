package cat.content.randomcat.api.fact

import cat.content.randomcat.api.fact.model.CatFact
import cat.content.randomcat.exeption.impl.CatFactApiException
import cat.content.randomcat.utils.ApiRequestHandler.parse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class CatFactApi(
    @Value("\${bot.cat-bot.api.fact.random-fact}") private var url: String,
    @Value("\${bot.cat-bot.api.fact.type}") private var type: String,
    @Value("\${bot.cat-bot.api.fact.amount}") private var amount: String,
    @Value("\${bot.cat-bot.api.fact.verified}") private var verified: Boolean
) {
    @Throws(CatFactApiException::class)
    fun randomFact(): CatFact {
        var catFacts: List<CatFact> = emptyList()
        fun getFacts() {
            try {
                catFacts = parse(
                    responseType,
                    url,
                    mapOf(TYPE to type, AMOUNT to amount),
                    HttpMethod.GET
                )?.filter { verified && it.status.verified == true } ?: emptyList()

                if (catFacts.isEmpty()) {
                    getFacts()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw CatFactApiException("Error while fetching data from cat fact api ($url)")
            }
        }

        getFacts()
        return catFacts.first()
    }

    companion object {
        private val responseType = object : ParameterizedTypeReference<List<CatFact>>() {}
        private const val TYPE = "animal_type"
        private const val AMOUNT = "amount"
    }
}