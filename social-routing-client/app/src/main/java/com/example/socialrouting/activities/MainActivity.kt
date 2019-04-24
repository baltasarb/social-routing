package com.example.socialrouting.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.socialrouting.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun create (view: View) {
        val intent = Intent(this, RouteCreationActivity::class.java)
        startActivity(intent)
    }

    fun search(view: View) {
        val intent = Intent(this, SearchRoutesActivity::class.java)
        startActivity(intent)
    }

    fun userProfile(view: View) {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

}
