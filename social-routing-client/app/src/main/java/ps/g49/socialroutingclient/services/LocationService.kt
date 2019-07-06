package ps.g49.socialroutingclient.services

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import ps.g49.socialroutingclient.activities.BaseActivity

class LocationService : Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val INTENT_FILTER = "GPSLocationUpdate"
        const val INTENT_MESSAGE = "Location"
        private const val LOCATION_INTERVAL: Long = 1000
        private const val LOCATION_DISTANCE: Float = 10f
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {
        if (provider == LocationManager.GPS_PROVIDER) {
            // request gps from user
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    sendLocation(it)
                }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location?) {
        sendLocation(location)
    }

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_INTERVAL,
                LOCATION_DISTANCE,
                this
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }

    private fun sendLocation(location: Location?) {
        if (location != null) {
            val intent = Intent(INTENT_FILTER)
            intent.putExtra(INTENT_MESSAGE, location)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}