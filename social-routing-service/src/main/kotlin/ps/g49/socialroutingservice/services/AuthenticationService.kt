package ps.g49.socialroutingservice.services

import org.apache.commons.codec.binary.Hex.encodeHexString
import org.apache.commons.codec.digest.MessageDigestAlgorithms
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.domainModel.GoogleAuthenticationData
import ps.g49.socialroutingservice.models.AuthenticationData
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import kotlin.experimental.and

@Service
class AuthenticationService(
        private val authenticationRepository: AuthenticationRepository
) {

    private val secureRandomAlgorithmName = "SHA1PRNG"

    /*fun googleSignIn(idTokenString : String) : GoogleAuthenticationData{
        *//*val idToken = googleAuthenticationService.validateAndGetIdToken(idTokenString) ?: throw GoogleAuthenticationException()
        val payload = idToken.payload
        val subject = payload.subject*//*
        val subject = "subject8"
        return authenticationRepository.findGooglePersonInfoAndCreateIfNotExists(subject, ::hashTokenToSHA256)
    }*/

    fun generateAuthenticationData(personIdentifier: Int): AuthenticationData {
        val accessToken = generateToken()
        val refreshToken = generateToken()
        val creationDate = System.currentTimeMillis()
        val expirationDate = creationDate + 9999999999
        return AuthenticationData(creationDate, expirationDate, accessToken, refreshToken, personIdentifier)
    }

    private fun generateToken(): String {
        val sha1Random = SecureRandom.getInstance("SHA1PRNG", "SUN")
        sha1Random.setSeed(SecureRandom().generateSeed(55))
        val values = ByteArray(55)
        sha1Random.nextBytes(values) // SHA1PRNG , seeded properly
        return bytesToHex(values)
    }

    private fun hashTokenToSHA256(token: String): String {
        val digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256)//todo : try sha3
        val stringBytes = token.toByteArray(StandardCharsets.UTF_8)
        val hashBytes = digest.digest(stringBytes)
        return bytesToHex(hashBytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexStringBuffer = StringBuffer()
        for (i in 0 until bytes.size) {
            hexStringBuffer.append(byteToHex(bytes[i]))
        }
        return hexStringBuffer.toString()
    }

    fun byteToHex(num: Byte): String {
        val hexDigits = CharArray(2)
        hexDigits[0] = Character.forDigit((num.toInt() shr 4) and 0xF, 16)
        hexDigits[1] = Character.forDigit(num.toInt() and 0xF, 16)
        return String(hexDigits)
    }

    fun getPersonAuthenticationData(connectionHandle: Handle, personIdentifier: Int): AuthenticationData? {
        return authenticationRepository.findAuthenticationDataById(connectionHandle, personIdentifier)
    }

    fun storeHashedAuthenticationData(connectionHandle: Handle, authenticationData: AuthenticationData) {
        authenticationRepository.createOrUpdateAuthenticationData(connectionHandle, authenticationData)
    }

    fun hashAuthenticationDataAndGet(authenticationData: AuthenticationData): AuthenticationData {
        return AuthenticationData(
                authenticationData.creationDate,
                authenticationData.expirationDate,
                hashTokenToSHA256(authenticationData.accessToken),
                hashTokenToSHA256(authenticationData.refreshToken),
                authenticationData.personIdentifier
        )
    }

}