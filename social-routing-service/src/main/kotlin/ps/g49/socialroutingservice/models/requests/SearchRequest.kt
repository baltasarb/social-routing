package ps.g49.socialroutingservice.models.requests

import ps.g49.socialroutingservice.exceptions.InvalidRouteSearchParameterException
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.GeographicPoint

data class SearchRequest(
        val location: String,// id da localizacao onde user se encontra
        val page: Int,
        val categories: List<Category>,
        val duration: String, //1hora / meio dia / 1 dia -- short/medium/long
        val coordinates: GeographicPoint? = null
) {

    companion object {
        fun build(params: HashMap<String, String>): SearchRequest {
            val page = params["page"]?.toInt()
            return SearchRequest(
                    location = verifyAndGetLocation(params["location"]),
                    page = page ?: 1,
                    categories = verifyAndGetCategories(params["categories"]),
                    coordinates = verifyAndGetPoint(params["latitude"], params["longitude"]),
                    duration = params["duration"]?: throw InvalidRouteSearchParameterException("Missing parameter, Duration is required when performing a search.")
            )
        }

        private fun verifyAndGetLocation(location: String?): String {
            if (location == null)
                throw InvalidRouteSearchParameterException("Missing parameter, Location is required when performing a search.")
            return location
        }

        private fun verifyAndGetCategories(categoriesString: String?): List<Category> {
            if (categoriesString == null) {
                throw InvalidRouteSearchParameterException("Missing parameter, at least one category is required when performing a search.")
            }
            return categoriesString.split(',').map { Category(it) }
        }

        private fun verifyAndGetPoint(latitude: String?, longitude: String?): GeographicPoint? {
            if ((latitude == null) or (longitude == null)) {
                return null
            }
            return (GeographicPoint(latitude!!.toDouble(), longitude!!.toDouble()))
        }

    }

}