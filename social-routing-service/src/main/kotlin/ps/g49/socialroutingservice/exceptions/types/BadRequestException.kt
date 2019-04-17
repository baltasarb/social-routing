package ps.g49.socialroutingservice.exceptions.types

class BadRequestException : JsonResponseException(
        "type breq",
        "title breq",
        2,
        "details breq",
        "instance breq"
)