package com.example.campusconnect.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.campusconnect.R
import com.example.campusconnect.model.LocationItem
import com.example.campusconnect.model.MapPath
import com.example.campusconnect.network.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var searchEditText: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationMap = mutableMapOf<String, LocationItem>()
    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        searchEditText = findViewById(R.id.searchLocationEditText)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString().trim()
            val result = locationMap.values.find {
                it.name.contains(query, ignoreCase = true) ||
                        it.building.contains(query, ignoreCase = true)
            }

            if (result != null) {
                val latLng = LatLng(result.latitude, result.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableUserLocation()
        loadLocations()
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f))

                map.addMarker(
                    MarkerOptions()
                        .position(userLatLng)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableUserLocation()
        }
    }

    private fun loadLocations() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val service = RetrofitClient.getClient(this@MapActivity)
                val locations = service.getLocations().locations
                val paths = service.getMapPaths().map_paths

                for (loc in locations) {
                    val position = LatLng(loc.latitude, loc.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(loc.name)
                            .snippet("${loc.building} - ${loc.floor} ${loc.room}")
                    )
                    locationMap[loc.name] = loc
                }

                drawPaths(paths)

            } catch (e: Exception) {
                Toast.makeText(this@MapActivity, "Failed to load map data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun drawPaths(paths: List<MapPath>) {
        for (path in paths) {
            val polyline = PolylineOptions().width(5f)
                .color(ContextCompat.getColor(this, R.color.primaryColor))

            for (point in path.path_data) {
                val lat = point["lat"] ?: continue
                val lng = point["lng"] ?: continue
                polyline.add(LatLng(lat, lng))
            }

            map.addPolyline(polyline)
        }
    }
}