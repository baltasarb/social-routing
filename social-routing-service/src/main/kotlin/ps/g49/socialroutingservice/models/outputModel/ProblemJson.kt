package ps.g49.socialroutingservice.models.outputModel

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson(
        val type: String,
        val title: String,
        val status: Int,
        val detail: String,
        val instance: String? = null
)