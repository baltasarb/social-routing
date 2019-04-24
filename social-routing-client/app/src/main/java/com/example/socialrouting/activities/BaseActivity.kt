package com.example.socialrouting.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.socialrouting.utils.Resource

abstract class BaseActivity : AppCompatActivity() {

    //private var spinnerId: Int = R.id.spinnerProgressBar

    protected fun <T> handleRequestedData(data: LiveData<Resource<T>>) {
        data.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    requestLoadingHandler()
                }
                Resource.Status.ERROR -> {
                    //stopSpinner()
                    requestErrorHandler(it.message!!)
                }
                Resource.Status.SUCCESS -> {
                    //stopSpinner()
                    requestSuccessHandler(it.data!!)
                }
            }
        })
    }

    /*
    protected fun startSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.VISIBLE
    }

    protected fun stopSpinner() {
        val spinner = findViewById<ProgressBar>(spinnerId)
        spinner.visibility = View.GONE
    }
    */

    protected open fun requestLoadingHandler() {
        // TODO Loading Handling
        //startSpinner()
    }

    protected open fun requestErrorHandler(errorMessage: String) {
        // ToDo Error Handling
    }

    protected abstract fun <T> requestSuccessHandler(result: T)
}