package ps.g49.socialroutingclient.utils

// A generic class that contains data and status about requestDefaultLoadingHandler this data.
class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T> success(): Resource<T> {
            return Resource(Status.SUCCESS, null, null)
        }
    }
}