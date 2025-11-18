package com.example.bouleto.vues


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Data class pour représenter un joueur
data class Joueur(
    val nom: String,
    val initiales: String,
    val points: Int,
    val couleurAvatar: Color
)


@Composable
fun Accueil() {
    val joueursExemple = listOf(
        Joueur("Sophie Martin", "SM", 32, Color(0xFFFFA726)),
        Joueur("Thomas Dubois", "TD", 28, Color(0xFF26C6DA)),
        Joueur("Julie Lefebvre", "JL", 19, Color(0xFFFFA726)),
        Joueur("Marc Petit", "MP", 15, Color(0xFF80CBC4)),
        Joueur("Emma Bernard", "EB", 12, Color(0xFFFFA726)),
        Joueur("Lucas Moreau", "LM", 9, Color(0xFF26C6DA)),
        Joueur("Camille Roux", "CR", 7, Color(0xFFFFA726))
    )

    TableauClassement(joueurs = joueursExemple)
}

@Composable
fun CartePerso(
    joueur: Joueur,
    position: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Partie gauche : icône position + avatar + infos
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icône de position ou numéro
            if (position <= 3) {
                val iconColor = when (position) {
                    1 -> Color(0xFFFFA726) // Orange
                    2 -> Color(0xFF26C6DA) // Cyan
                    3 -> Color(0xFFFFA726) // Orange pour 3ème aussi
                    else -> Color.Gray
                }

                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Position $position",
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                // Afficher le numéro pour les positions 4+
                Text(
                    text = "#$position",
                    fontSize = 16.sp,
                    color = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(28.dp)
                )
            }

            // Avatar avec initiales
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(joueur.couleurAvatar),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = joueur.initiales,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Nom et points
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = joueur.nom,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "${joueur.points} pts",
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            }
        }

        // Badge de points à droite
        val badgeColor = when (position) {
            1 -> Color(0xFFFFA726) // Orange
            2 -> Color(0xFF26C6DA) // Cyan
            3 -> Color(0xFFFFA726) // Orange
            else -> Color(0xFF9E9E9E) // Gris
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
                    text = joueur.points.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = badgeColor
                )
            }
        }
    }
}


@Composable
fun TableauClassement(joueurs: List<Joueur>) {

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
            // En-tête du tableau qui scrolle
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
                        tint = Color(0xFFFFA726),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Classement",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                }
            }

            // Liste des joueurs
            itemsIndexed(joueurs) { index, joueur ->
                CartePerso(
                    joueur = joueur,
                    position = index + 1
                )
            }
        }
    }
}

