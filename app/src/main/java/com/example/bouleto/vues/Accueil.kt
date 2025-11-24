package com.example.bouleto.vues

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bouleto.MainViewmodel
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.Membre
import kotlin.math.abs

@Composable
fun Accueil(groupe: Groupe, viewModel: MainViewmodel) {
    // On trie par points décroissants
    var membresTries = groupe.membres.sortedByDescending { it.scoreTotal }
    val groupeSupprime = remember { mutableStateOf(false) }

    LaunchedEffect(groupe.id) {
        groupeSupprime.value = false
    }
    if (membresTries.isEmpty() || groupeSupprime.value) {
        EcranAucunGroupe()
        return
    }
    else{
        TableauClassement(membres = membresTries, groupe = groupe, viewModel = viewModel, onGroupeSupprime = { groupeSupprime.value = true } )
    }

}


@Composable
fun CartePerso(
    membre: Membre,
    position: Int
) {
    // 1. Calcul des initiales
    val initiales = remember(membre.pseudo) {
        val mots = membre.pseudo.trim().split(" ").filter { it.isNotEmpty() }

        if (mots.size >= 2) {
            "${mots[0].first().uppercaseChar()}${mots[1].first().uppercaseChar()}"
        } else if (mots.isNotEmpty() && mots[0].length >= 2) {
            mots[0].take(2).uppercase()
        } else {
            mots.firstOrNull()?.first()?.uppercase()?.repeat(2) ?: "??"
        }
    }

    // 2. Génération de couleur aléatoire stable (basée sur le hash du nom)
//    val couleurAvatar = remember(membre.id) {
//        genererCouleurAleatoire(membre.pseudo + membre.nom)
//    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Partie gauche
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icône de position
            if (position <= 3) {
                // ✅ Sélectionner l'icône et la couleur selon la position
                val (iconVector, iconColor) = when (position) {
                    1 -> Icons.Outlined.EmojiEvents to Color(0xFFDAA520)  // Or (Diamond)
                    2 -> Icons.Outlined.WorkspacePremium to Color(0xFFC0C0C0)  // Argent (WineBar)
                    3 -> Icons.Outlined.MilitaryTech to Color(0xFFCC6633)     // Bronze (Star)
                    else -> Icons.Outlined.Star to Color.Gray
                }

                Icon(
                    imageVector = iconVector,
                    contentDescription = "Position $position",
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(
                    text = "#$position",
                    fontSize = 16.sp,
                    color = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(28.dp)
                )
            }

            // Avatar Coloré
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(membre.couleur)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initiales,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Infos
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${membre.pseudo} ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "${membre.scoreTotal} pts",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            }
        }

        // Badge de points à droite
        val badgeColor = when (position) {
            1 -> Color(0xFFFFB300)
            2 -> Color(0xFF90CAF9)
            3 -> Color(0xFFEF9A9A)
            else -> Color(0xFF9E9E9E)
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, badgeColor),
            color = Color.White,
            modifier = Modifier.defaultMinSize(minWidth = 48.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = membre.scoreTotal.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = badgeColor
                )
            }
        }
    }
}

@Composable
fun TableauClassement(membres: List<Membre>, groupe: Groupe, viewModel: MainViewmodel, onGroupeSupprime: () -> Unit) {

    var afficherDialogueSupprimer = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f) // ✅ Prend tout l'espace disponible
                .background(Color.White)
        ) {
            // 📌 Titre
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Classement",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Classement Boulets",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                }
            }

            // 📋 Liste des membres
            itemsIndexed(membres) { index, membre ->
                CartePerso(
                    membre = membre,
                    position = index + 1
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // ✅ Centre horizontalement
        ) {
            // 🗑️ Bouton en bas - ✅ EN DEHORS de LazyColumn
            OutlinedButton(
                onClick = { afficherDialogueSupprimer.value = true },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(16.dp)
                    .height(40.dp),

                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red,
                    containerColor = Color.White
                ),

                border = BorderStroke(1.dp, Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Supprimer ce groupe",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 🔔 Dialogue de confirmation
            if (afficherDialogueSupprimer.value) {
                DialogueConfirmationSuppression(
                    nomGroupe = groupe.nom,
                    onConfirmer = {
                        viewModel.deleteGroupe(id = groupe.id)
                        onGroupeSupprime()
                        afficherDialogueSupprimer.value = false
                    },
                    onAnnuler = {
                        afficherDialogueSupprimer.value = false
                    }
                )
            }
        }
    }
}



@Composable
fun EcranAucunGroupe() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icône décorative
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color(0xFFFFA726).copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Titre
            Text(
                text = "Aucun groupe sélectionné",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texte explicatif
            Text(
                text = "Pour commencer à jouer :",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Étape 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0xFFFFA726),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Créer un groupe",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "Rends toi sur le Menu Burger sur les 3 traits ou en swipant vers la droite. Puis" +
                                "appuie sur \"Créer un nouveau groupe\" en haut. Ajoute des membres, un pseudo et une couleur chacun !",
                        fontSize = 14.sp,
                        color = Color(0xFF888888),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Étape 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0xFFFFA726),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "2",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sélectionner le groupe",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "Clique sur la carte du groupe pour commencer à compter les points de tes membres !",
                        fontSize = 14.sp,
                        color = Color(0xFF888888),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Message encourageant
            Text(
                text = "🎯 C'est parti pour traquer les boulets !",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFFA726),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DialogueConfirmationSuppression(
    nomGroupe: String,
    onConfirmer: () -> Unit,
    onAnnuler: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onAnnuler,
        containerColor = Color.White,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF5252),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = "Confirmer la suppression",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Êtes-vous sûr de vouloir supprimer le groupe :",
                    fontSize = 16.sp,
                    color = Color(0xFF546E7A)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\"$nomGroupe\"",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Cette action est irréversible.",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFFF5252)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmer,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5252).copy(alpha = 0.8f)
                ),
                //shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Supprimer", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onAnnuler,
                modifier = Modifier.padding(end = 10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF546E7A)
                ),
               // shape = RoundedCornerShape(8.dp)
            ) {
                Text("Annuler", fontWeight = FontWeight.Medium)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}


