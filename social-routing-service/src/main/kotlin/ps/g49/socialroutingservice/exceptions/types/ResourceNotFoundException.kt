package ps.g49.socialroutingservice.exceptions.types


class ResourceNotFoundException : JsonResponseException(
        "type nfound",
        "title nfound",
        1,
        "details nfound",
        "instance nfound"
)