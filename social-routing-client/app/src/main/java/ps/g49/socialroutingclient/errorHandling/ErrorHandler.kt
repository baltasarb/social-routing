package ps.g49.socialroutingclient.errorHandling

interface ErrorHandler {

    fun proceed(error: Throwable)

    fun attachView(view: CanShowError)

    fun detachView()

}