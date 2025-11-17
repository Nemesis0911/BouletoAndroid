package com.example.bouleto.vues

import androidx.compose.material.icons.outlined.Add
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults


data class Membre(
    val prenom: String,
    val nom: String,
    val id: String = "${System.currentTimeMillis()}_${(0..1000).random()}"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaireGroupe(
    onDismiss: () -> Unit,
    onValider: (String, List<Membre>) -> Unit
) {
    var nomGroupe by remember { mutableStateOf("") }
    var prenomActuel by remember { mutableStateOf("") }
    var nomActuel by remember { mutableStateOf("") }
    var membres by remember { mutableStateOf(listOf<Membre>()) }

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
                // Header avec icône et titre
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

                // Section Membres du groupe avec bouton Ajouter
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
                            if (prenomActuel.isNotBlank() && nomActuel.isNotBlank()) {
                                membres = membres + Membre(prenomActuel, nomActuel)
                                prenomActuel = ""
                                nomActuel = ""
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

                // Champs Prénom et Nom + X pour supprimer
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 190.dp),


                ) {
                    // Ligne d'ajout active
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth(),

                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = prenomActuel,
                                onValueChange = { prenomActuel = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text("Prénom", color = Color.Gray, fontSize = 14.sp)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedContainerColor = Color(0xFFF5F5F5)
                                )


                            )

                            OutlinedTextField(
                                value = nomActuel,
                                onValueChange = { nomActuel = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text("Nom", color = Color.Gray, fontSize = 14.sp)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedContainerColor = Color(0xFFF5F5F5)
                                )

                            )

                            // Icône X (invisible pour la ligne d'ajout)
                            Box(modifier = Modifier.size(20.dp))
                        }
                    }

                    // Membres déjà ajoutés
                    itemsIndexed(membres) { index, membre ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = membre.prenom,
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedContainerColor = Color(0xFFF5F5F5)
                                )

                            )

                            OutlinedTextField(
                                value = membre.nom,
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedContainerColor = Color(0xFFF5F5F5)
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
                Button(
                    onClick = {
                        if (nomGroupe.isNotBlank() && membres.isNotEmpty()) {
                            onValider(nomGroupe, membres)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    enabled = nomGroupe.isNotBlank() && membres.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA726)
                    ),


                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Créer le groupe",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bouton Annuler
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = "Annuler",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
