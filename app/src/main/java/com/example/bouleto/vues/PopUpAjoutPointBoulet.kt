package com.example.bouleto.vues

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.ApiResponse
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.bouleto.models.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpAjoutPointBoulet(
    onDismiss: () -> Unit,
    onConfirm: (Membre, Int, String? ,Double ,Double) -> Unit,
    groupeSelectionnee: Groupe,
    viewModel: MainViewmodel
) {
    var membreSelectionne by remember { mutableStateOf<Membre?>(null) }
    var nombrePoints by remember { mutableStateOf("2") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val membres = groupeSelectionnee.membres

    var adresseRecherche by remember { mutableStateOf("") }
    var adresseSelectionnee by remember { mutableStateOf<Result?>(null) }
    var showSuggestions by remember { mutableStateOf(false) }

    // ✅ Observer le StateFlow
    val resultatsApi by viewModel.resultatApi.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // En-tête
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Ajouter un point boulet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sélection du membre
                Text(
                    text = "Sélectionner un membre",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                // ✅ BOX AVEC DROPDOWN CLASSIQUE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }  // ✅ Toute la box est cliquable
                ) {
                    OutlinedTextField(
                        value = membreSelectionne?.let { "${it.prenom} ${it.nom}" } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Sélectionner un membre") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded)
                                    Icons.Default.KeyboardArrowUp
                                else
                                    Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.DarkGray  // ✅ Flèche gris foncé
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            //disabledBorderColor = Color.DarkGray,  // ✅ Bordure gris foncé
                            disabledTextColor = Color.Black,  // ✅ Texte noir
                            disabledPlaceholderColor = Color.Gray,  // ✅ Placeholder gris
                            disabledTrailingIconColor = Color.DarkGray,  // ✅ Icône gris foncé
                            focusedBorderColor = Color(0xFFFFB300),
                            unfocusedBorderColor = Color.LightGray
                        ),
                        enabled = false
                    )

                    // ✅ DROPDOWN MENU AVEC LARGEUR LIMITÉE
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .heightIn(max = 200.dp)
                            .background(Color.White)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    ) {
                        if (membres.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Aucun membre dans ce groupe") },
                                onClick = { },
                                enabled = false
                            )
                        } else {
                            membres.forEach { membre ->
                                DropdownMenuItem(
                                    text = { Text("${membre.prenom} ${membre.nom}") },
                                    onClick = {
                                        membreSelectionne = membre
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre de points
                Text(
                    text = "Nombre de points",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombrePoints,
                    onValueChange = { if (it.all { char -> char.isDigit() }) nombrePoints = it },
                    placeholder = { Text("Ex: 2") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFB300),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                //Champ adresse
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Adresse de l'action (optionnel)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // ✅ Box avec position relative pour le dropdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        OutlinedTextField(
                            value = adresseRecherche,
                            onValueChange = { nouvelleValeur ->
                                adresseRecherche = nouvelleValeur
                                adresseSelectionnee = null

                                if (nouvelleValeur.length >= 3) {
                                    showSuggestions = true
                                    viewModel.rechercheAdresse(nouvelleValeur)
                                } else {
                                    showSuggestions = false
                                }
                            },
                            placeholder = {
                                Text("Commencez à taper une adresse...", color = Color.Gray)
                            },
                            trailingIcon = {
                                if (adresseRecherche.isNotEmpty()) {
                                    IconButton(onClick = {
                                        adresseRecherche = ""
                                        adresseSelectionnee = null
                                        showSuggestions = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Effacer",
                                            tint = Color.Black
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFB300),
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            singleLine = true
                        )

                        // ✅ DROPDOWN QUI S'OUVRE VERS LE BAS
                        if (resultatsApi.results.isNotEmpty()) {

                            DropdownMenu(
                                expanded = showSuggestions,
                                onDismissRequest = { showSuggestions = false },
                                properties = PopupProperties(
                                    focusable = false,  // ✅ Garde le focus sur le TextField
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = true
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.72f)
                                    .heightIn(max = 250.dp)
                                    .background(Color.White)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            ) {
                                resultatsApi.results.forEach { adresse ->
                                    DropdownMenuItem(
                                        text = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = adresse?.fulltext ?: "",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    text = "${adresse.city}",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        },
                                        onClick = {
                                            adresseRecherche = adresse?.fulltext ?: ""
                                            adresseSelectionnee = adresse
                                            showSuggestions = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    if (adresse != resultatsApi.results.last()) {
                                        Divider(
                                            color = Color.White,
                                            thickness = 0.5.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                // ✅ AFFICHAGE ADRESSE SÉLECTIONNÉE
//                if (adresseSelectionnee != null) {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = CardDefaults.cardColors(
//                            containerColor = Color(0xFF2D2D2D)
//                        )
//                    ) {
//                        Column(modifier = Modifier.padding(12.dp)) {
//                            Text(
//                                text = "Adresse sélectionnée :",
//                                fontSize = 12.sp,
//                                color = Color.Gray
//                            )
//                            Text(
//                                text = adresseSelectionnee!!.fulltext ?: "",
//                                fontSize = 14.sp,
//                                color = Color.White,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    }
//                }
                Spacer(modifier = Modifier.height(8.dp))
                // Description
                Text(
                    text = "Description de l'action (optionnel)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Ex: A oublié ses clés pour la 3ème fois...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFB300),
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Boutons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Annuler", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            membreSelectionne?.let { membre ->
                                onConfirm(
                                    membre,
                                    nombrePoints.toIntOrNull() ?: 0,
                                    description,
                                    adresseSelectionnee?.x ?: 0.0,
                                    adresseSelectionnee?.y ?: 0.0
                                )
                                Toast.makeText(context, "Point ajouté !", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB300)
                        ),
                        enabled = membreSelectionne != null
                    ) {
                        Text("Ajouter les points")
                    }
                }
            }
        }
    }


}



