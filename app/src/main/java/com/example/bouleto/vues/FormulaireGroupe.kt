package com.example.bouleto.vues

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.Membre

// 🎨 Liste de couleurs prédéfinies
val couleursDisponibles = listOf(

    Color(0xFFFFB3BA), // 🔴 Rose corail
    Color(0xFFFFDAB9), // 🧡 Pêche
    Color(0xFFFFFACD), // 💛 Jaune citron
    Color(0xFFB4E7CE), // 💚 Vert menthe
    Color(0xFFADD8E6), // 💙 Bleu ciel
    Color(0xFFD4BAFF), // 💜 Lavande
    Color(0xFFFFB6D9), // 💗 Rose bonbon
    Color(0xFFFFE5B4), // 🥐 Beige pêche
    Color(0xFFC7F0BD), // 🍃 Vert pomme
    Color(0xFFB4D4FF)  // 🐬 Bleu pervenche

)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaireGroupe(
    onDismiss: () -> Unit,
    onValider: (String, List<Membre>) -> Unit,
    viewModel: MainViewmodel
) {
    var nomGroupe by remember { mutableStateOf("") }
    var pseudoActuel by remember { mutableStateOf("") }
    var prenomActuel by remember { mutableStateOf("") }
    var nomActuel by remember { mutableStateOf("") }
    var membres by remember { mutableStateOf(listOf<Membre>()) }

    // 🎨 États pour le sélecteur de couleur
    var afficherSelecteurCouleur by remember { mutableStateOf(false) }
    var membreIndexAModifier by remember { mutableStateOf<Int?>(null) }
    var couleurNouveauMembre by remember { mutableStateOf(couleursDisponibles.random()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            tint = Color(0xFFFFA726),
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Créer un nouveau groupe",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nom du groupe
                Text(
                    text = "Nom du groupe",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                OutlinedTextField(
                    value = nomGroupe,
                    onValueChange = { nomGroupe = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Ex: Famille, Amis, Bureau...",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Section Membres
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Membres du groupe",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )

                    TextButton(
                        onClick = {
                            // ✅ Ajoute le membre avec la couleur sélectionnée
                            if (pseudoActuel.isNotBlank()) {
                                val nouveauMembre = Membre(
                                    pseudo = pseudoActuel,
                                    couleur = couleurNouveauMembre.toArgb().toLong() // ✅ Sauvegarde en Long
                                )
                                membres = membres + nouveauMembre

                                // Reset des champs
                                pseudoActuel = ""
                                prenomActuel = ""
                                nomActuel = ""
                                couleurNouveauMembre = couleursDisponibles.random()
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFF00BCD4)
                        )
                    ) {
                        Text(
                            text = "+ Ajouter",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Liste des membres
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                ) {
                    // ✅ Ligne d'ajout avec rond de couleur
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 🎨 Rond de couleur cliquable
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(couleurNouveauMembre)
                                    .border(2.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                                    .clickable {
                                        membreIndexAModifier = null
                                        afficherSelecteurCouleur = true
                                    }
                            )

                            OutlinedTextField(
                                value = pseudoActuel,
                                onValueChange = { pseudoActuel = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text("Pseudo", color = Color.Gray, fontSize = 14.sp)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedContainerColor = Color(0xFFF5F5F5)
                                )
                            )

                            // Espace pour l'icône de suppression (alignement)
                            Box(modifier = Modifier.size(24.dp))
                        }
                    }

                    // ✅ Membres déjà ajoutés
                    itemsIndexed(membres) { index, membre ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 🎨 Rond de couleur du membre (utilise l'extension .couleur)
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(membre.couleur)) // ✅ Utilise l'extension
                                    .border(2.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                                    .clickable {
                                        membreIndexAModifier = index
                                        afficherSelecteurCouleur = true
                                    }
                            )

                            OutlinedTextField(
                                value = membre.pseudo ?: "",
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Color.LightGray,
                                    disabledContainerColor = Color(0xFFF5F5F5),
                                    disabledTextColor = Color.Black
                                )
                            )

                            IconButton(
                                onClick = {
                                    membres = membres.filterIndexed { i, _ -> i != index }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Supprimer",
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bouton Créer le groupe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Bouton Annuler à gauche
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        // shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Gray
                        ),

                        ) {
                        Text(
                            text = "Annuler",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Bouton Créer le groupe à droite
                    val context = LocalContext.current // ✅ Ajoute ceci AVANT le Button

                    Button(
                        onClick = {
                            // Ajoute le dernier membre si les champs sont remplis
                            var membresFinaux = membres
                            if (pseudoActuel.isNotBlank()) {
                                membresFinaux = membresFinaux + Membre(
                                    pseudo = pseudoActuel,
                                    couleur = couleurNouveauMembre.toArgb().toLong()
                                )
                            }

                            if (nomGroupe.isNotBlank() && membresFinaux.isNotEmpty()) {
                                // ✅ TOAST ICI
                                Toast.makeText(
                                    context,
                                    "Groupe \"$nomGroupe\" créé avec ${membresFinaux.size} membre(s) !",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onValider(nomGroupe, membresFinaux)
                                onDismiss()
                            } else {
                                // ✅ TOAST D'ERREUR (optionnel)
                                Toast.makeText(
                                    context,
                                    "Veuillez remplir tous les champs",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .height(40.dp),
                        enabled = nomGroupe.isNotBlank() && (membres.isNotEmpty() || pseudoActuel.isNotBlank()),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFA726)
                        ),
                    ) {
                        Text(
                            text = "Créer le groupe",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }


    // 🎨 Popup de sélection de couleur
    if (afficherSelecteurCouleur) {
        SelecteurCouleur(
            onDismiss = { afficherSelecteurCouleur = false },
            onCouleurSelectionnee = { couleur ->
                if (membreIndexAModifier == null) {
                    // Changement pour le nouveau membre
                    couleurNouveauMembre = couleur
                } else {
                    // Changement pour un membre existant
                    membres = membres.mapIndexed { index, membre ->
                        if (index == membreIndexAModifier) {
                            membre.copy(couleur = couleur.toArgb().toLong()) // ✅ Sauvegarde en Long
                        } else {
                            membre
                        }
                    }
                }
                afficherSelecteurCouleur = false
            }
        )
    }
}


// 🎨 Composant Sélecteur de Couleur
@Composable
fun SelecteurCouleur(
    onDismiss: () -> Unit,
    onCouleurSelectionnee: (Color) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Choisir une couleur",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(couleursDisponibles) { couleur ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(couleur)
                                .border(2.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)
                                .clickable {
                                    onCouleurSelectionnee(couleur)
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Annuler", color = Color.Gray)
                }
            }
        }
    }
}
