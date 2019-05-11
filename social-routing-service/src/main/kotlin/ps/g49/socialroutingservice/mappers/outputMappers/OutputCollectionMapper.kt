package ps.g49.socialroutingservice.mappers.outputMappers

interface OutputCollectionMapper<T, R> {

    fun mapCollection(list: List<T>): R

}