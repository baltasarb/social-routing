package com.example.socialrouting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.socialrouting.services.SocialRoutingApiService
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test()
    }

    fun test() {
        val url = "https://api.github.com/users/events"
        val app = getSocialRoutingApplication()

        SocialRoutingApiService(app).requestTest(url, {

            textView.text = it.toString(2)

        }, {

            textView.text = it

        })
    }

    fun getSocialRoutingApplication(): SocialRoutingApplication
            = this@MainActivity.application as SocialRoutingApplication

}
