package ps.g49.socialroutingclient.activities

import android.Manifest
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
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
                    requestDefaultLoadingHandler()
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
                    requestDefaultLoadingHandler()
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
                    requestDefaultLoadingHandler()
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
        requestSuccessHandler: (result: T?) -> Unit,
        requestLoadingHandler: () -> Unit,
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

    protected fun <T> handleRequestedData(
        data: LiveData<Resource<T>>,
        requestSuccessHandler: (result: T?) -> Unit,
        requestErrorHandler: () -> Unit
    ) {
        data.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    requestDefaultLoadingHandler()
                }
                Resource.Status.ERROR -> {
                    stopSpinner()
                    requestErrorHandler()
                }
                Resource.Status.SUCCESS -> {
                    stopSpinner()
                    requestSuccessHandler(it.data)
                }
            }
        })
    }

    protected abstract fun initView()

    protected fun <T> startNewActivity(cls: Class<T>, finish: Boolean) {
        val intent = Intent(this, cls)
        startActivity(intent)
        if (finish)
            finish()
    }

    protected fun startSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.VISIBLE
    }

    protected fun stopSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.GONE
    }

    protected open fun requestDefaultLoadingHandler() {
        startSpinner()
    }

    protected open fun requestErrorHandler(errorMessage: String) {
        if (errorMessage.isNotEmpty())
            showToast(errorMessage)
    }

    protected open fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}