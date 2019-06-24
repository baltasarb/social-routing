package ps.g49.socialroutingclient.activities

import android.Manifest
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dagger.android.support.DaggerAppCompatActivity
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.utils.Resource

abstract class BaseActivity : DaggerAppCompatActivity() {

    companion object {
        const val LOCATION_PERMISSION_REQUEST = 1234
        val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val spinnerId: Int = R.id.spinnerProgressBar

    protected fun <T> handleRequestedData(
        data: LiveData<Resource<T>>,
        requestSuccessHandler: (result: T?) -> Unit
    ) {
        data.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    requestLoadingHandler()
                }
                Resource.Status.ERROR -> {
                    stopSpinner()
                    requestErrorHandler(it.message!!)
                }
                Resource.Status.SUCCESS -> {
                    stopSpinner()
                    requestSuccessHandler(it.data)
                }
            }
        })
    }

    protected fun <T> handleRequestedData(
        data: LiveData<Resource<T>>,
        requestSuccessHandler: () -> Unit
    ) {
        data.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    requestLoadingHandler()
                }
                Resource.Status.ERROR -> {
                    stopSpinner()
                    requestErrorHandler(it.message!!)
                }
                Resource.Status.SUCCESS -> {
                    stopSpinner()
                    requestSuccessHandler()
                }
            }
        })
    }

    protected fun <T> handleRequestedData(
        data: LiveData<Resource<T>>,
        requestSuccessHandler: (result: T?) -> Unit,
        requestErrorHandler: (msg: String) -> Unit
    ) {
        data.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    requestLoadingHandler()
                }
                Resource.Status.ERROR -> {
                    stopSpinner()
                    requestErrorHandler(it.message!!)
                }
                Resource.Status.SUCCESS -> {
                    stopSpinner()
                    requestSuccessHandler(it.data)
                }
            }
        })
    }

    protected fun startSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.VISIBLE
    }

    protected fun stopSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.GONE
    }

    protected open fun requestLoadingHandler() {
        startSpinner()
    }

    protected open fun requestErrorHandler(errorMessage: String) {
        // ToDo Error Handling
    }

    protected open fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}