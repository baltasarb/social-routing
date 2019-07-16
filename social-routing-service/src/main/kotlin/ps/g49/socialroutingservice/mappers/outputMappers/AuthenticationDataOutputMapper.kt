package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData
import ps.g49.socialroutingservice.models.outputModel.AuthenticationDataOutput

@Component
class AuthenticationDataOutputMapper : OutputMapper<AuthenticationData, AuthenticationDataOutput> {
    override fun map(from: AuthenticationData): AuthenticationDataOutput {
        return AuthenticationDataOutput(from.accessToken, from.refreshToken)
    }
}