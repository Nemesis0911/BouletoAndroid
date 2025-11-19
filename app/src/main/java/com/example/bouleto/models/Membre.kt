package com.example.bouleto.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Membre(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prenom: String,
    val nom: String,
    var points: Int = 0,
)
