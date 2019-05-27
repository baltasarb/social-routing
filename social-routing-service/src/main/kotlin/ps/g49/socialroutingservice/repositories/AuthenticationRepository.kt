package ps.g49.socialroutingservice.repositories

import ps.g49.socialroutingservice.models.domainModel.GooglePersonInfo

interface AuthenticationRepository {

    fun findGooglePersonInfoAndCreateIfNotExists(
            subject: String,
            hashToken: (String) -> String
    ): GooglePersonInfo

    fun validateServerGeneratedTokenAndSubject(hashedToken : String, subject: String) : Boolean

}