package ps.g49.socialroutingservice.exceptions.types

import java.lang.Exception

abstract class JsonResponseException (
        val type: String,
        val title: String,
        val status: Int,
        val detail: String,
        val instance: String
) : Exception()