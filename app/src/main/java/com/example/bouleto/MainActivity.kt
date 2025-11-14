package com.example.bouleto

import android.R.attr.icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.bouleto.ui.theme.BouletoTheme
import com.example.bouleto.vues.Accueil
import com.example.bouleto.vues.Carte
import com.example.bouleto.vues.Parametres

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
//

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
fun Main() {
    val backStack = remember { mutableStateListOf<Any>(DestinationAccueil()) }

    Scaffold(
        topBar = {TopBarAccueil(backStack)},
        bottomBar = { BottomNavigationBar(backStack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ){
                Button(
                    onClick = { /* Action */ },
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

            }
            Spacer(modifier = Modifier.height(8.dp))
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (key) {
                        is DestinationAccueil -> NavEntry(key) {
                            Accueil()
                        }
                        is DestinationCarte -> NavEntry(key) {
                            Carte()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarAccueil(backStack: SnapshotStateList<Any>) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Bouleto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                val current = backStack.lastOrNull()
                val titleText = when (current) {
                    is DestinationAccueil -> "Tableau des boulets"
                    is DestinationCarte -> "Carte des boulets"
                    is DestinationParametres -> "Paramétres"
                    else -> "Error"
                }
                Text(titleText,fontSize = 14.sp,
                    color = Color.Gray)
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Open menu */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions ={
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFA726),
                modifier = Modifier
                    .size(50.dp)
                //.padding(end = 10.dp)


            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Trophée",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)

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