package com.example.socialrouting

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class SocialRoutingApplication : Application() {

    // TODO: token from Social Routing Server
    lateinit var token: String
    lateinit var queue: RequestQueue

    override fun onCreate() {
        super.onCreate()

        queue = Volley.newRequestQueue(this)
        token = "c005bef494e39dd2db6d56854bcba0c8e0ecec24"

    }

}