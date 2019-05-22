package ps.g49.socialroutingclient.activities

import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.utils.Resource

abstract class BaseActivity : AppCompatActivity() {

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


    protected fun startSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.VISIBLE
    }

    protected fun stopSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.GONE
    }

    protected open fun requestLoadingHandler() {
        // TODO Loading Handling
        //startSpinner()
    }

    protected open fun requestErrorHandler(errorMessage: String) {
        // ToDo Error Handling
    }

    protected open fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}