package com.example.bouleto.vues

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre
import com.example.bouleto.models.Result
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PopUpAjoutPointBoulet(
    onDismiss: () -> Unit,
    onConfirm: (Membre, Int, String?, Double, Double) -> Unit,
    groupeSelectionnee: Groupe,
    viewModel: MainViewmodel
) {
    // ✅ Variables d'état
    var membreSelectionne by remember { mutableStateOf<Membre?>(null) }
    var nombrePoints by remember { mutableStateOf("2") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var adresseRecherche by remember { mutableStateOf("") }
    var adresseSelectionnee by remember { mutableStateOf<Result?>(null) }
    var showSuggestions by remember { mutableStateOf(false) }

    // ✅ Observer le StateFlow
    val resultatsApi by viewModel.resultatApi.collectAsState()

    val context = LocalContext.current
    val membres = groupeSelectionnee.membres

    // ✅ Observer la localisation
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isLoadingLocation by viewModel.isLoadingLocation.collectAsState()

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // ✅ Mettre à jour les champs quand la position GPS arrive
    LaunchedEffect(currentLocation) {
        currentLocation?.let { (lat, lon) ->
            latitude = lat.toString()
            longitude = lon.toString()
            android.util.Log.d("PopUpAjout", "📍 GPS reçu: Lat=$lat, Lon=$lon")
        }
    }

    // ✅ Mettre à jour les champs quand une adresse est sélectionnée
    LaunchedEffect(adresseSelectionnee) {
        adresseSelectionnee?.let { adresse ->
            latitude = adresse.y.toString()  // y = latitude
            longitude = adresse.x.toString()  // x = longitude
            android.util.Log.d("PopUpAjout", "📍 Adresse sélectionnée: ${adresse.fulltext}")
            android.util.Log.d("PopUpAjout", "   Coordonnées: Lat=${adresse.y}, Lon=${adresse.x}")
        }
    }

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        viewModel.updateLocationPermission(locationPermissionState.allPermissionsGranted)
    }

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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
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
                        Icon(Icons.Default.Close, contentDescription = "Fermer")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sélection du membre
                Text(
                    text = "Sélectionner un membre",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = membreSelectionne?.pseudo ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Sélectionner un membre") },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFB300),
                            unfocusedBorderColor = Color.LightGray,
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.LightGray
                        ),
                        enabled = false
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .heightIn(max = 200.dp)
                    ) {
                        if (membres.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Aucun membre") },
                                onClick = {},
                                enabled = false
                            )
                        } else {
                            membres.forEach { membre ->
                                DropdownMenuItem(
                                    text = { Text(membre.pseudo) },
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
                Text(text = "Nombre de points", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombrePoints,
                    onValueChange = { if (it.all { char -> char.isDigit() }) nombrePoints = it },
                    placeholder = { Text("Ex: 2") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFB300),
                        unfocusedBorderColor = Color.LightGray
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ BOUTON GPS
                Button(
                    onClick = {
                        if (locationPermissionState.allPermissionsGranted) {
                            viewModel.getCurrentLocation()
                        } else {
                            locationPermissionState.launchMultiplePermissionRequest()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    enabled = !isLoadingLocation
                ) {
                    if (isLoadingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Localisation...")
                    } else {
                        Icon(Icons.Default.LocationOn, contentDescription = "GPS", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Utiliser ma position GPS")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ RECHERCHE D'ADRESSE
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Ou rechercher une adresse",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        val focusRequester = remember { FocusRequester() }
                        val focusManager = LocalFocusManager.current

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
                            placeholder = { Text("Ex: 10 Rue de Rivoli, Paris") },
                            trailingIcon = {
                                if (adresseRecherche.isNotEmpty()) {
                                    IconButton(onClick = {
                                        adresseRecherche = ""
                                        adresseSelectionnee = null
                                        showSuggestions = false
                                    }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Effacer")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize = coordinates.size
                                },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFB300),
                                unfocusedBorderColor = Color.LightGray
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )

                        // ✅ DROPDOWN DES SUGGESTIONS
                        DropdownMenu(
                            expanded = resultatsApi.results.isNotEmpty() && showSuggestions,
                            onDismissRequest = { showSuggestions = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                .heightIn(max = 200.dp)
                        ) {
                            resultatsApi.results.forEach { adresse ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = adresse.fulltext.toString(),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = adresse.city ?: "",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    },
                                    onClick = {
                                        adresseRecherche = adresse.fulltext.toString()
                                        adresseSelectionnee = adresse
                                        showSuggestions = false
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Latitude/Longitude (lecture seule)
                Text(text = "Coordonnées (automatique)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = latitude,
                        onValueChange = {},
                        label = { Text("Latitude") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color.LightGray,
                            disabledTextColor = Color.Black
                        ),
                        enabled = false
                    )

                    OutlinedTextField(
                        value = longitude,
                        onValueChange = {},
                        label = { Text("Longitude") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color.LightGray,
                            disabledTextColor = Color.Black
                        ),
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(text = "Description (optionnel)", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Ex: A oublié ses clés...") },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
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
                                val lat = latitude.toDoubleOrNull() ?: 0.0
                                val lon = longitude.toDoubleOrNull() ?: 0.0

                                android.util.Log.d("PopUpAjout", "🔵 Sauvegarde: Lat=$lat, Lon=$lon")

                                onConfirm(membre, nombrePoints.toIntOrNull() ?: 2, description.ifBlank { null }, lat, lon)
                                Toast.makeText(context, "Point ajouté !", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                        enabled = membreSelectionne != null && latitude.isNotEmpty() && longitude.isNotEmpty()
                    ) {
                        Text("Ajouter les points")
                    }
                }
            }
        }
    }
}
