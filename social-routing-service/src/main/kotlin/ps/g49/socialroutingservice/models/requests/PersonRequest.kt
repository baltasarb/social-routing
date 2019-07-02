package ps.g49.socialroutingservice.models.requests

import ps.g49.socialroutingservice.models.inputModel.PersonInput

data class PersonRequest(
        var identifier: Int? = null,
        val rating: Double? = null
){
    companion object{
        fun build(personInput: PersonInput, id: Int? = null): PersonRequest {
            return PersonRequest(
                    identifier = id,
                    rating = personInput.rating
            )
        }
    }
}