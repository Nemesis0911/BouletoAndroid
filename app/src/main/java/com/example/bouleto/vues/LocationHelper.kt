package com.example.bouleto.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * ✅ FONCTION NORMALE (pas suspend)
     * Récupère la position actuelle avec callbacks
     */
    fun getCurrentLocation(
        onSuccess: (latitude: Double, longitude: Double) -> Unit,
        onFailure: (error: String) -> Unit
    ) {
        // Vérifier les permissions
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onFailure("Permission GPS non accordée")
            return
        }

        // Récupérer la dernière position connue d'abord (plus rapide)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("LocationHelper", "✅ Dernière position: ${location.latitude}, ${location.longitude}")
                    onSuccess(location.latitude, location.longitude)
                } else {
                    // Si pas de dernière position, demander une position actuelle
                    requestCurrentLocation(onSuccess, onFailure)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("LocationHelper", "❌ Erreur lastLocation", exception)
                // Essayer quand même la position actuelle
                requestCurrentLocation(onSuccess, onFailure)
            }
    }

    /**
     * Demande une position GPS actuelle (peut prendre quelques secondes)
     */
    private fun requestCurrentLocation(
        onSuccess: (latitude: Double, longitude: Double) -> Unit,
        onFailure: (error: String) -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onFailure("Permission GPS non accordée")
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("LocationHelper", "✅ Position actuelle: ${location.latitude}, ${location.longitude}")
                onSuccess(location.latitude, location.longitude)
            } else {
                onFailure("Impossible d'obtenir la position GPS")
            }
        }.addOnFailureListener { exception ->
            Log.e("LocationHelper", "❌ Erreur getCurrentLocation", exception)
            onFailure("Erreur GPS: ${exception.message}")
        }
    }
}
