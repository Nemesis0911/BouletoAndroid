package com.example.bouleto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre
import com.example.bouleto.vues.*
import kotlinx.coroutines.launch

class DestinationAccueil
class DestinationCarte
class DestinationParametres

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Main()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarAccueil(
    backStack: SnapshotStateList<Any>,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(end = 10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Bouleto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(0.dp))

                val current = backStack.lastOrNull()
                val titleText = when (current) {
                    is DestinationAccueil -> "Tableau des boulets"
                    is DestinationCarte -> "Carte des boulets"
                    is DestinationParametres -> "Paramètres"
                    else -> "Error"
                }
                Text(
                    titleText,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        },
        navigationIcon = {
            // ✅ TOUJOURS VISIBLE maintenant
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFA726),
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Trophée",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun BottomNavigationBar(backStack: SnapshotStateList<Any>) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val current = backStack.lastOrNull()
        val activeTab = when (current) {
            is DestinationAccueil -> 1
            is DestinationCarte -> 2
            is DestinationParametres -> 3
            else -> 0
        }

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Accueil"
                )
            },
            label = { Text("Accueil") },
            selected = activeTab == 1,
            onClick = { backStack.add(DestinationAccueil()) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFFFA726),
                selectedTextColor = Color(0xFFFFA726),
                indicatorColor = Color(0xFFFFA726).copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "Maps"
                )
            },
            label = { Text("Maps") },
            selected = activeTab == 2,
            onClick = { backStack.add(DestinationCarte()) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFFFA726),
                selectedTextColor = Color(0xFFFFA726),
                indicatorColor = Color(0xFFFFA726).copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Paramètres"
                )
            },
            label = { Text("Paramètres") },
            selected = activeTab == 3,
            onClick = { backStack.add(DestinationParametres()) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFFFA726),
                selectedTextColor = Color(0xFFFFA726),
                indicatorColor = Color(0xFFFFA726).copy(alpha = 0.1f)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val backStack = remember { mutableStateListOf<Any>(DestinationAccueil()) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var afficherDialogPoint = remember { mutableStateOf(false) }
    val viewModel = viewModel<MainViewmodel>()

    val groupeSelectionne = viewModel.groupeSelectionne.collectAsState()
    viewModel.getAll()
    val groupes = viewModel.groupes.collectAsState().value

    // 🎯 Déterminer si on est sur la page Carte
    val current = backStack.lastOrNull()
    val estSurCarte = current is DestinationCarte

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !estSurCarte,  // ✅ Swipe désactivé sur la carte
        drawerContent = {
            MenuGroupes(
                groupes = groupes,
                onClose = { scope.launch { drawerState.close() } },
                onCreerGroupe = { scope.launch { drawerState.close() } },
                onCloseMenu = {
                    scope.launch { drawerState.close() }
                },
                viewmodel = viewModel()
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBarAccueil(
                    backStack = backStack,
                    onMenuClick = { scope.launch { drawerState.open() } }
                    // ✅ Plus de paramètre showMenuButton
                )
            },
            bottomBar = { BottomNavigationBar(backStack) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Button(
                        onClick = { afficherDialogPoint.value = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFA726)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ajouter un point boulet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (afficherDialogPoint.value) {
                        PopUpAjoutPointBoulet(
                            onDismiss = {
                                afficherDialogPoint.value = false
                            },
                            onConfirm = { membreSelectionne, pointsAjoutes, description, latitude, longitude ->
                                // ✅ Mise à jour du groupe avec les coordonnées GPS
                                Log.d("MainActivity", "🟢 onConfirm reçu:")
                                Log.d("MainActivity", "   - Membre: ${membreSelectionne.pseudo}")
                                Log.d("MainActivity", "   - Points: $pointsAjoutes")
                                Log.d("MainActivity", "   - Description: $description")
                                Log.d("MainActivity", "   - Latitude: $latitude")
                                Log.d("MainActivity", "   - Longitude: $longitude")
                                viewModel.updateGroupe(
                                    groupe = groupeSelectionne.value,
                                    membre = membreSelectionne,
                                    score = pointsAjoutes,
                                    lat = latitude,
                                    long = longitude,

                                )

                                afficherDialogPoint.value = false

                                // ✅ Rafraîchir les données
                                scope.launch {
                                    kotlinx.coroutines.delay(200)
                                    viewModel.getAll()
                                }
                            },
                            groupeSelectionnee = groupeSelectionne.value,
                            viewModel = viewModel
                        )
                    }

                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(color = Color.White)
                        .weight(1f)
                ) {
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when (key) {
                                is DestinationAccueil -> NavEntry(key) {
                                    val groupeAJour =
                                        groupes.find { it.id == groupeSelectionne.value.id }
                                            ?: groupeSelectionne.value
                                    Accueil(groupe = groupeAJour, viewModel = viewModel)
                                }

                                is DestinationCarte -> NavEntry(key) {
                                    Carte(
                                        membres = groupeSelectionne.value.membres,
                                        viewModel = viewModel
                                    )
                                }

                                is DestinationParametres -> NavEntry(key) {
                                    Parametres()
                                }

                                else -> {
                                    error("Unknown key $key")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Option 1", "Option 2", "Option 3").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selected = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
