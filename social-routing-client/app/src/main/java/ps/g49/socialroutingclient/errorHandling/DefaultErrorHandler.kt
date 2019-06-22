package ps.g49.socialroutingclient.errorHandling

import java.lang.ref.WeakReference
import javax.inject.Inject

class DefaultErrorHandler @Inject constructor(
    //private val resourceManager: ResourceManager
) : ErrorHandler{

    private lateinit var view: WeakReference<CanShowError>

    override fun proceed(error: Throwable) {

    }

    override fun attachView(view: CanShowError) {
    }

    override fun detachView() {

    }
}