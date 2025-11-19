package com.example.bouleto.vues

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre

@Composable
fun Parametres(viewModel: MainViewmodel){
    viewModel.addGroupe(
        Groupe(
            nom = "Enfin réussi ?",
            membres = listOf(Membre(
                prenom = "Joshua",
                nom = "Six"
            )),
            couleur = Color.Red
        )
    )

    val groupes = viewModel.groupes.collectAsState()


    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(groupes.value) { it ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Nom du groupe : ${it.nom}")
                }
            }
        }
    }
}