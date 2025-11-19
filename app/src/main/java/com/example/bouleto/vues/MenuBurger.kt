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


// Modèle de données pour les groupes
data class GroupeOld(
    val nom: String,
    val membreOlds: List<MembreOld> = emptyList(),  // ← AJOUTE CETTE LIGNE
    val nbMembres: Int = membreOlds.size,  // ← Calculé automatiquement
    val initiales: String,
    val couleur: Color,
    val estSelectionne: Boolean = false
)

@Composable
fun MenuGroupes(
    groupeOlds: SnapshotStateList<GroupeOld>,
    onClose: () -> Unit,
    onCreerGroupe: () -> Unit,
    onSelectGroupe: (GroupeOld) -> Unit
) {
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
                items(groupeOlds) { groupe ->
                    CarteGroupe(
                        groupeOld = groupe,
                        onClick = { onSelectGroupe(groupe) }
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
                    onValider = { nomGroupe, membres ->
                        val initiales = nomGroupe.take(2).uppercase()
                        val couleurs = listOf(
                            Color(0xFFFFA726), Color(0xFF26A69A), Color(0xFFEC407A),
                            Color(0xFF42A5F5), Color(0xFF9CCC65), Color(0xFF26A69A)
                        )
                        val couleur = couleurs.random()

                        groupeOlds.add(
                            GroupeOld(
                                nom = nomGroupe,
                                nbMembres = membres.size,
                                initiales = initiales,
                                couleur = couleur,
                                estSelectionne = false
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
    groupeOld: GroupeOld,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (groupeOld.estSelectionne) 2.dp else 1.dp,
                color = if (groupeOld.estSelectionne) Color(0xFFFFA726) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .background(if (groupeOld.estSelectionne) Color(0xFFFFF8E1) else Color.White)
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
                    .background(groupeOld.couleur),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = groupeOld.initiales,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column {
                Text(
                    text = groupeOld.nom,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "${groupeOld.nbMembres} membre${if (groupeOld.nbMembres > 1) "s" else ""}",
                    fontSize = 14.sp,
                    color = Color(0xFF999999)
                )
            }
        }

        if (groupeOld.estSelectionne) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Sélectionné",
                tint = Color(0xFFFFA726),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
