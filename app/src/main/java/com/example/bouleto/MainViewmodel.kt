package com.example.bouleto

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre
import com.example.bouleto.models.Point
import com.example.bouleto.repository.BddRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewmodel(application: Application): AndroidViewModel(application){

    //récupération du répository
    val bddRepository = BddRepository(application)

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


    fun deleteGroupe(id : Int) {
        viewModelScope.launch {
            bddRepository.deleteGroupe(id)
            getAll()
        }
    }

    fun clearDatabase(){
        viewModelScope.launch {
            bddRepository.deleteAll()
            getAll()
        }
    }

    fun setGroupeSelectionne(groupe : Groupe){
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

    //liste de nos groupes
    val groupes = MutableStateFlow<List<Groupe>>(emptyList())

    val groupeSelectionne = MutableStateFlow<Groupe>(Groupe(id = -1, nom = "test", couleur = androidx.compose.ui.graphics.Color.Blue))
}

