package ps.g49.socialroutingservice.services

import org.apache.commons.codec.digest.MessageDigestAlgorithms
import org.apache.logging.log4j.core.util.UuidUtil.*
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.exceptions.GoogleAuthenticationException
import ps.g49.socialroutingservice.models.domainModel.GooglePersonInfo
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Service
class AuthenticationService(
        private val authenticationRepository: AuthenticationRepository,
        private val googleAuthenticationService: GoogleAuthenticationService
) {

    fun googleAuthenticationIsValid(token: String, subject: String): Boolean {
        val hashedToken = hashTokenToSHA256(token)
        return googleAuthenticationService.validateServerGeneratedTokenAndSubject(hashedToken, subject)
    }

    fun googleSignIn(idTokenString : String) : GooglePersonInfo{
        /*val idToken = googleAuthenticationService.validateAndGetIdToken(idTokenString) ?: throw GoogleAuthenticationException()
        val payload = idToken.payload
        val subject = payload.subject*/
        val subject = "subject8"
        return authenticationRepository.findGooglePersonInfoAndCreateIfNotExists(subject, ::hashTokenToSHA256)
    }

    fun hashTokenToSHA256(token: String): String {
        val digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256)//todo : try sha3
        val stringBytes = token.toByteArray(StandardCharsets.UTF_8)
        val hashBytes = digest.digest(stringBytes)
        return bytesToHex(hashBytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes.indices) {
            //if number is < 16 '0{number}' else the number will come out with correct hex
            val hexString = String.format("%02x", b)
            sb.append(hexString);
        }
        return sb.toString();
    }

}