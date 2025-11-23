package com.example.bouleto.models

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity
data class Membre(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Json(name = "pseudo")
    val pseudo: String = "",  // ✅ Valeur par défaut vide


    // ✅ Stocke comme Long dans la base de données
    @ColumnInfo(name = "couleur")
    val couleur: Long = 0xFF101010,

    var point: List<Point> = listOf(),
) {
    // 🎨 Propriété calculée pour Compose (ignorée par Moshi ET Room)
    fun getCouleur(): Color = Color(couleur.toULong())
    val scoreTotal: Int
        get() = point.sumOf { it.score }
}



