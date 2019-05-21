package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ps.g49.socialroutingclient.R

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
        val intent = Intent(this, RouteSearchActivity::class.java)
        startActivity(intent)
    }

    fun userProfile(view: View) {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

}
