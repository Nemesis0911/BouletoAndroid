package com.example.bouleto.vues

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Parametres() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // En-tête
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null,
                tint = Color(0xFFFFA726),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Paramètres",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // 🎨 INTERFACE
        SectionParametres(
            title = "Interface",
            icon = Icons.Filled.Palette
        ) {
            ParametreTheme()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🗺️ CARTE
        SectionParametres(
            title = "Carte",
            icon = Icons.Filled.Map
        ) {
            ParametreCentrage()
            Spacer(modifier = Modifier.height(12.dp))
            ParametreMemorisation()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📌 MARQUEURS
        SectionParametres(
            title = "Marqueurs",
            icon = Icons.Filled.Place
        ) {
            ParametreTailleMarqueurs()
            Spacer(modifier = Modifier.height(12.dp))
            ParametreRegroupement()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📍 LOCALISATION
        SectionParametres(
            title = "Localisation",
            icon = Icons.Filled.LocationOn
        ) {
            ParametreLocalisation()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Message d'info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = Color(0xFFFFA726)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Ces fonctionnalités seront bientôt disponibles !",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFA726)
                )
            }
        }
    }
}

@Composable
fun SectionParametres(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFFFA726),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

// 🎨 THÈME
@Composable
fun ParametreTheme() {
    var themeSelectionne by remember { mutableStateOf("Clair") }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Brightness6,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Thème de l'application",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = themeSelectionne == "Clair",
                onClick = { /* Désactivé */ },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LightMode,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clair")
                    }
                },
                enabled = false,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = themeSelectionne == "Sombre",
                onClick = { /* Désactivé */ },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.DarkMode,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sombre")
                    }
                },
                enabled = false,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = themeSelectionne == "Auto",
                onClick = { /* Désactivé */ },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AutoMode,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Auto")
                    }
                },
                enabled = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// 🗺️ CENTRAGE CARTE
@Composable
fun ParametreCentrage() {
    var centrageActif by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.MyLocation,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Centrer sur ma position",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "Au démarrage de l'application",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        }
        Switch(
            checked = centrageActif,
            onCheckedChange = { /* Désactivé */ },
            enabled = false,
            colors = SwitchDefaults.colors(
                disabledCheckedThumbColor = Color(0xFFBDBDBD),
                disabledUncheckedThumbColor = Color(0xFFBDBDBD)
            )
        )
    }
}

// 💾 MÉMORISATION
@Composable
fun ParametreMemorisation() {
    var memorisationActive by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.SaveAlt,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Mémoriser la dernière vue",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "Garder zoom et position",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        }
        Switch(
            checked = memorisationActive,
            onCheckedChange = { /* Désactivé */ },
            enabled = false,
            colors = SwitchDefaults.colors(
                disabledCheckedThumbColor = Color(0xFFBDBDBD),
                disabledUncheckedThumbColor = Color(0xFFBDBDBD)
            )
        )
    }
}

// 📏 TAILLE MARQUEURS
@Composable
fun ParametreTailleMarqueurs() {
    var tailleMarqueur by remember { mutableFloatStateOf(1f) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.PhotoSizeSelectLarge,
                    contentDescription = null,
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Taille des marqueurs",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Text(
                text = when {
                    tailleMarqueur < 0.5f -> "Petit"
                    tailleMarqueur < 1.5f -> "Moyen"
                    else -> "Grand"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFFA726),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = tailleMarqueur,
            onValueChange = { /* Désactivé */ },
            valueRange = 0f..2f,
            steps = 3,
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                disabledThumbColor = Color(0xFFBDBDBD),
                disabledActiveTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

// 🎯 REGROUPEMENT
@Composable
fun ParametreRegroupement() {
    var regroupementActif by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Workspaces,
                contentDescription = null,
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Regrouper les points proches",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "Clustering automatique",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        }
        Switch(
            checked = regroupementActif,
            onCheckedChange = { /* Désactivé */ },
            enabled = false,
            colors = SwitchDefaults.colors(
                disabledCheckedThumbColor = Color(0xFFBDBDBD),
                disabledUncheckedThumbColor = Color(0xFFBDBDBD)
            )
        )
    }
}

// 📍 LOCALISATION
@Composable
fun ParametreLocalisation() {
    var localisationActive by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOff,
                contentDescription = null,
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Désactiver la localisation",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "Empêcher l'accès au GPS",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        }
        Switch(
            checked = !localisationActive,
            onCheckedChange = { /* Désactivé */ },
            enabled = false,
            colors = SwitchDefaults.colors(
                disabledCheckedThumbColor = Color(0xFFBDBDBD),
                disabledUncheckedThumbColor = Color(0xFFBDBDBD)
            )
        )
    }
}
