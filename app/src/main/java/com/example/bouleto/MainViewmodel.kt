package com.example.bouleto

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bouleto.helpers.LocationHelper
import com.example.bouleto.models.ApiResponse
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre
import com.example.bouleto.models.Point
import com.example.bouleto.repository.ApiRepository
import com.example.bouleto.repository.BddRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewmodel(application: Application): AndroidViewModel(application) {

    // Récupération des repositories
    val bddRepository = BddRepository(application)
    val apiRepository = ApiRepository()

    private val locationHelper: LocationHelper = LocationHelper(application.applicationContext)

    // StateFlows pour la géolocalisation
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation.asStateFlow()

    private val _isLoadingLocation = MutableStateFlow(false)
    val isLoadingLocation: StateFlow<Boolean> = _isLoadingLocation.asStateFlow()

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    // Liste de nos groupes
    val groupes = MutableStateFlow<List<Groupe>>(emptyList())

    val groupeSelectionne = MutableStateFlow<Groupe>(
        Groupe(
            id = -1,
            nom = "test",
            couleur = androidx.compose.ui.graphics.Color.Blue
        )
    )

    // API
    val resultatApi = MutableStateFlow<ApiResponse>(ApiResponse(results = emptyList(), status = ""))

    init {
        Log.d("MainViewModel", "✅ ViewModel initialisé")
        Log.d("MainViewModel", "✅ LocationHelper créé")
        getAll()
    }

    fun getAll() {
        viewModelScope.launch {
            groupes.value = bddRepository.getAll()
        }
    }

    fun addGroupe(groupe: Groupe) {
        viewModelScope.launch {
            val membresAvecId = groupe.membres.mapIndexed { index, membre ->
                membre.copy(id = System.currentTimeMillis().toInt() + index)
            }

            val groupeAvecIds = groupe.copy(membres = membresAvecId)

            bddRepository.addGroupe(groupeAvecIds)
            getAll()
        }
    }

    fun deleteGroupe(id: Int) {
        viewModelScope.launch {
            bddRepository.deleteGroupe(id)

            // ✅ Filtre la liste
            groupes.value = groupes.value.filter { it.id != id }

            // ✅ NOUVEAU : Réinitialise le groupe sélectionné si c'est celui supprimé
            if (groupeSelectionne.value.id == id) {
                groupeSelectionne.value = Groupe(
                    id = -1,
                    nom = "",
                    couleur = androidx.compose.ui.graphics.Color.Gray,
                    membres = emptyList() // ⚠️ Liste vide = rien à afficher
                )
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            bddRepository.deleteAll()
            getAll()
        }
    }

    fun setGroupeSelectionne(groupe: Groupe) {
        groupeSelectionne.value = groupe
    }

    fun updateGroupe(groupe: Groupe, membre: Membre, score: Int, lat: Double, long: Double) {
        val point = Point(score = score, latidute = lat, longitude = long)
        Log.d("updateGroupe", "Point ajouté: ${point.score}")
        var membreSelec = groupe.membres.filter { it.id == membre.id }.first()
        membreSelec.point = membreSelec.point.plus(point)

        viewModelScope.launch {
            bddRepository.updateGroupe(groupe)
            getAll()
        }
    }

    // API - Recherche adresse
    fun rechercheAdresse(adress: String) {
        viewModelScope.launch {
            resultatApi.value = apiRepository.rechercheAdresse(adress)
            Log.d(
                "rechercheAdresse",
                "Adresse trouvée: ${apiRepository.rechercheAdresse(adress).results.firstOrNull()}"
            )
        }
    }

    // ✅ FONCTION getCurrentLocation (modifiée avec des logs)
    fun getCurrentLocation() {
        Log.d("MainViewModel", "🔵 getCurrentLocation() appelée")

        _isLoadingLocation.value = true

        locationHelper.getCurrentLocation(
            onSuccess = { latitude, longitude ->
                Log.d("MainViewModel", "✅ Position reçue: Lat=$latitude, Lon=$longitude")

                _currentLocation.value = Pair(latitude, longitude)
                _isLoadingLocation.value = false

                Log.d("MainViewModel", "📍 currentLocation mis à jour: ${_currentLocation.value}")
            },
            onFailure = { error ->
                Log.e("MainViewModel", "❌ Erreur GPS: $error")

                _isLoadingLocation.value = false
            }
        )
    }

    fun updateLocationPermission(granted: Boolean) {
        _locationPermissionGranted.value = granted
        Log.d("MainViewModel", "🔐 Permission GPS: $granted")
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "🔴 ViewModel détruit")
    }
}
