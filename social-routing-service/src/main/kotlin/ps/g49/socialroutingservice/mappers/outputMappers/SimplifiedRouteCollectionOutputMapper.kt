package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteCollection
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteCollectionOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class SimplifiedRouteCollectionOutputMapper (private val mapper : SimplifiedRouteOutputMapper): OutputMapper<SimplifiedRouteCollection, SimplifiedRouteCollectionOutput> {

    override fun map(from: SimplifiedRouteCollection): SimplifiedRouteCollectionOutput {
        val page = from.nextPage
        return SimplifiedRouteCollectionOutput(
                if(page == null) null else OutputUtils.personRoutesUrlWithPage(from.routes.first().personIdentifier, page),
                mapper.mapCollection(from.routes)
        )
    }

    fun mapSearch(from : SimplifiedRouteCollection, params : HashMap<String, String>) : SimplifiedRouteCollectionOutput{
        val page = from.nextPage
        var next : String? = null

        if(page != null){
            next = OutputUtils.searchRoutesUrlWithParams(params, page)
        }

        return SimplifiedRouteCollectionOutput(
                next,
                mapper.mapCollection(from.routes)
        )
    }


}