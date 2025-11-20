package com.example.bouleto.vues

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre


@Composable
fun MenuGroupes(
    groupes: List<Groupe>,
    onClose: () -> Unit,
    onCreerGroupe: () -> Unit,
    onCloseMenu: () -> Unit,
    viewmodel: MainViewmodel
) {

    val groupeSelectionne = viewmodel.groupeSelectionne.collectAsState()

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.85f),
        drawerContainerColor = Color.White
    ) {
        var afficherFormulaire by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // En-tête "Mes groupes"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Mes groupes",
                        tint = Color(0xFFFFA726),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Mes groupes",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = Color(0xFF666666)
                    )
                }
            }

            // Bouton "Créer un nouveau groupe"
            Button(
                onClick = { afficherFormulaire = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA726)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Créer un nouveau groupe",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "Vos groupes"
            Text(
                text = "Vos groupes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 12.dp)
            )



            // Liste des groupes
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(groupes) { groupe ->
                    CarteGroupe(
                        groupe = groupe,
                        membre = groupe.membres[0],
                        onClick = { viewmodel.setGroupeSelectionne(groupe = groupe); onCloseMenu() },
                        groupeSelectionne = groupeSelectionne
                    )
                }
            }

            // Message du bas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "💡", fontSize = 20.sp)
                Text(
                    text = "Créez des groupes pour organiser vos boulets par thème : famille, amis, travail...",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )
            }
            if (afficherFormulaire) {
                FormulaireGroupe(
                    onDismiss = { afficherFormulaire = false },
                    viewModel = viewModel(),
                    onValider = { nomGroupe, membres ->
                        val couleurs = listOf(
                            Color(0xFFc0f0ee), Color(0xFFfebb5f), Color(0xFFfe9d15),
                            Color(0xFF2dbdb4),
                        )
                        val couleur = couleurs.random()

                        viewmodel.addGroupe(
                            Groupe(
                                nom = nomGroupe,
                                membres = membres,
                                couleur = couleur,
                            )
                        )
                        afficherFormulaire = false
                    }
                )
            }
        }
    }
}



@Composable
fun CarteGroupe(
    groupe: Groupe,
    onClick: () -> Unit,
    membre: Membre,
    groupeSelectionne: State<Groupe>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (groupeSelectionne.value.id == groupe.id) 2.dp else 1.dp,
                color = if (groupeSelectionne.value.id == groupe.id) Color(0xFFFFA726) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .background(if (groupeSelectionne.value.id == groupe.id) Color(0xFFFFF8E1) else Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(groupe.couleur),
                contentAlignment = Alignment.Center
            ) {

                val initiales = remember(groupe.nom) {
                    val mots = groupe.nom.trim().split(" ").filter { it.isNotEmpty() }

                    if (mots.size >= 2) {
                        "${mots[0].first().uppercaseChar()}${mots[1].first().uppercaseChar()}"
                    } else if (mots.isNotEmpty() && mots[0].length >= 2) {
                        mots[0].take(2).uppercase()
                    } else {
                        mots.firstOrNull()?.first()?.uppercase()?.repeat(2) ?: "??"
                    }
                }
                Text(
                    text = initiales,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column {
                Text(
                    text = groupe.nom,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "${groupe.membres.size} membre${if (groupe.membres.size > 1) "s" else ""}",
                    fontSize = 14.sp,
                    color = Color(0xFF999999)
                )
            }
        }

        if (groupeSelectionne.value.id == groupe.id) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Sélectionné",
                tint = Color(0xFFFFA726),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
