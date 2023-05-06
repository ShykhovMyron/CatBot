package cat.content.randomcat.api.fact.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CatFact(
    @JsonProperty("status") val status: Status,
    @JsonProperty("_id") val id: String,
    @JsonProperty("__v") val version: Int,
    @JsonProperty("text") val text: String,
    @JsonProperty("source") val source: String?,
    @JsonProperty("updatedAt") val updatedAt: String?,
    @JsonProperty("type") val type: String?,
    @JsonProperty("createdAt") val createdAt: String?,
    @JsonProperty("deleted") val deleted: Boolean?,
    @JsonProperty("used") val used: Boolean?,
    @JsonProperty("user") val user: String?
)

data class Status(
    @JsonProperty("verified") val verified: Boolean?,
    @JsonProperty("feedback") val feedback: String?,
    @JsonProperty("sentCount") val sentCount: Int?,
)