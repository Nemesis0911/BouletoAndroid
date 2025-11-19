package com.example.bouleto

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bouleto.models.Groupe
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
    fun addGroupe(groupe : Groupe) {
        viewModelScope.launch {
            bddRepository.addGroupe(groupe)
            getAll()
        }
    }
    fun deleteGroupe(id : Int) {
        viewModelScope.launch {
            bddRepository.deleteGroupe(id)
            getAll()
        }
    }

    //liste de nos groupes
    val groupes = MutableStateFlow<List<Groupe>>(emptyList())

}

