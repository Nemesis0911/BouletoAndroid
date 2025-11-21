package com.example.bouleto.vues

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bouleto.models.Membre
import kotlin.math.abs

@Composable
fun Accueil(membres: List<Membre>) {
    // On trie par points décroissants
    val membresTries = membres.sortedByDescending { it.scoreTotal }

    TableauClassement(membres = membresTries)
}

@Composable
fun CartePerso(
    membre: Membre,
    position: Int
) {
    // 1. Calcul des initiales
    val initiales = remember(membre.prenom, membre.nom) {
        val p = membre.prenom.firstOrNull()?.toString() ?: ""
        val n = membre.nom.firstOrNull()?.toString() ?: ""
        (p + n).uppercase()
    }

    // 2. Génération de couleur aléatoire stable (basée sur le hash du nom)
    val couleurAvatar = remember(membre.id) {
        genererCouleurAleatoire(membre.prenom + membre.nom)
    }

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
                val iconColor = when (position) {
                    1 -> Color(0xFFFFB300) // Or
                    2 -> Color(0xFF90CAF9) // Argent/Bleu clair
                    3 -> Color(0xFFEF9A9A) // Bronze/Rouge clair
                    else -> Color.Gray
                }
                Icon(
                    imageVector = Icons.Outlined.Star,
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
                    modifier = Modifier.width(28.dp) // Largeur fixe pour alignement
                )
            }

            // Avatar Coloré
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(couleurAvatar),
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
                    text = "${membre.prenom} ${membre.nom}",
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
fun TableauClassement(membres: List<Membre>) {
    Column (
        modifier = Modifier
            .padding(10.dp,5.dp,10.dp,5.dp)
            .clip(RoundedCornerShape(12.dp))
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
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

            itemsIndexed(membres) { index, membre ->
                CartePerso(
                    membre = membre,
                    position = index + 1
                )
            }
        }
    }
}

// Fonction utilitaire pour générer une couleur sympa
fun genererCouleurAleatoire(cle: String): Color {
    val couleurs = listOf(
        Color(0xFFfe9d15),
        Color(0xFFc0f0ee),
        Color(0xFFfebb5f),
        Color(0xFF2dbdb4),

    )
    // On utilise le hashCode de la chaîne (ex: "JeanDupont") pour choisir toujours la même couleur pour la même personne
    val index = abs(cle.hashCode()) % couleurs.size
    return couleurs[index]
}
