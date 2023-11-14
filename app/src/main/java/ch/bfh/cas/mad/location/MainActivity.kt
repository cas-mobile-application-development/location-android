package ch.bfh.cas.mad.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var textViewLocation: TextView
    private lateinit var buttonLocation: Button
    private lateinit var locationPersmissionRequest: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonLocation = findViewById(R.id.button_location)
        textViewLocation = findViewById(R.id.textview_location)
        locationPersmissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                when {
                    isGranted -> getLocation()
                    else -> {
                        locationPersmissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                }
            }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        buttonLocation.setOnClickListener {
            getLocation()
        }
    }

    override fun onPause() {
        super.onPause()
        buttonLocation.setOnClickListener(null)
    }

    private fun getLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPersmissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    textViewLocation.text =
                        getString(R.string.location_text, location.latitude, location.longitude)
                }
            }.addOnCompleteListener {
                println("getting location failed")
            }
    }
}

