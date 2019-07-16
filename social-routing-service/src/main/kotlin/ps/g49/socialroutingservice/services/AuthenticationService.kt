package ps.g49.socialroutingservice.services

import org.apache.commons.codec.digest.MessageDigestAlgorithms
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.exceptions.TokenExpiredException
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * communicates with the authentication related repository and is used by the controllers
 */
@Service
class AuthenticationService(
        private val authenticationRepository: AuthenticationRepository
) {

    private val tokenDuration: Int = 86400000 // 1 day in milliseconds

    fun getPersonAuthenticationDataByRefreshToken(connectionHandle: Handle, refreshToken: String): AuthenticationData? {
        return authenticationRepository.findAuthenticationDataByRefreshToken(connectionHandle, refreshToken)
    }

    fun storeHashedAuthenticationData(connectionHandle: Handle, authenticationData: AuthenticationData) {
        authenticationRepository.createAuthenticationData(connectionHandle, authenticationData)
    }

    fun generateAuthenticationData(personIdentifier: Int): AuthenticationData {
        val accessToken = generateToken()
        val refreshToken = generateToken()
        val creationDate = System.currentTimeMillis()
        val expirationDate = creationDate + tokenDuration
        return AuthenticationData(creationDate, expirationDate, accessToken, refreshToken, personIdentifier)
    }

    /**
     * generates a token in hexadecimal using secure random and converting the resulting bytes to hex
     */
    private fun generateToken(): String {
        val sha1Random = SecureRandom.getInstance("SHA1PRNG", "SUN")
        sha1Random.setSeed(SecureRandom().generateSeed(55))
        val values = ByteArray(55)
        sha1Random.nextBytes(values) // SHA1PRNG , seeded properly
        return bytesToHex(values)
    }

    /**
     * converts the received bytes to an hexadecimal string
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexStringBuffer = StringBuffer()
        for (i in 0 until bytes.size) {
            hexStringBuffer.append(byteToHex(bytes[i]))
        }
        return hexStringBuffer.toString()
    }

    /**
     * converts a single byte to hexadecimal
     */
    fun byteToHex(num: Byte): String {
        val hexDigits = CharArray(2)
        hexDigits[0] = Character.forDigit((num.toInt() shr 4) and 0xF, 16)
        hexDigits[1] = Character.forDigit(num.toInt() and 0xF, 16)
        return String(hexDigits)
    }

    /**
     * hashed a user's authenticated data so that is can be stored
     */
    fun hashAuthenticationDataAndGet(authenticationData: AuthenticationData): AuthenticationData {
        return AuthenticationData(
                authenticationData.creationDate,
                authenticationData.expirationDate,
                hashTokenToSHA256(authenticationData.accessToken),
                hashTokenToSHA256(authenticationData.refreshToken),
                authenticationData.personIdentifier
        )
    }

    /**
     * hashes a received token to SHA256
     * @return hexadecimal string of the hashed token
     */
    fun hashTokenToSHA256(token: String): String {
        val digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256)//todo : try sha3
        val stringBytes = token.toByteArray(StandardCharsets.UTF_8)
        val hashBytes = digest.digest(stringBytes)
        return bytesToHex(hashBytes)
    }

    /**
     * @return the identifier of the user that haves the corresponding access token
     * @throws TokenExpiredException if the access token is expired
     */
    fun verifyTokenAndGetPersonIdentifier(accessToken: String): Int {
        val hashedAccessToken = hashTokenToSHA256(accessToken)
        val authenticationData = authenticationRepository.findAuthenticationDataByAccessToken(hashedAccessToken)

        if (authenticationData.accessTokenIsExpired())
            throw TokenExpiredException()

        return authenticationData.personIdentifier
    }

}