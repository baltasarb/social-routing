package ps.g49.socialroutingservice.models.domainModel

class AuthenticationData(
        val creationDate: Long,
        val expirationDate: Long,
        val accessToken: String,
        val refreshToken: String,
        val personIdentifier: Int
) {

    override fun equals(other: Any?): Boolean {
        val otherAuthenticationData = other as AuthenticationData
        return (accessToken == otherAuthenticationData.accessToken) and
                (refreshToken == otherAuthenticationData.refreshToken) and
                (personIdentifier == otherAuthenticationData.personIdentifier)
    }

    fun accessTokenIsExpired(): Boolean {
        return creationDate > expirationDate
    }

    override fun hashCode(): Int {
        var result = creationDate.hashCode()
        result = 31 * result + expirationDate.hashCode()
        result = 31 * result + accessToken.hashCode()
        result = 31 * result + refreshToken.hashCode()
        result = 31 * result + personIdentifier
        return result
    }
}