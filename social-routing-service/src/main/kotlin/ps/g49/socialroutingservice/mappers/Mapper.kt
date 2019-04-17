package ps.g49.socialroutingservice.mappers

interface Mapper <T, R>{

    fun map(from : T) : R

}