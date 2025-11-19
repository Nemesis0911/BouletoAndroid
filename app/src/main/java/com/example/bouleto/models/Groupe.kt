package com.example.bouleto.models

import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.Query
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity
data class Groupe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String,
    val membres: List<Membre> = emptyList(),
    val couleur: Color,
)

@ProvidedTypeConverter
class Convertisseur(moshi: Moshi) {
    private val membreAdapter = moshi.adapter<List<Membre>>(
        Types.newParameterizedType(List::class.java, Membre::class.java)
    )

    // Convertisseur pour List<Membre>
    @TypeConverter
    fun stringToMembre(value: String): List<Membre>? {
        return membreAdapter.fromJson(value)
    }

    @TypeConverter
    fun membreToString(membre: List<Membre>): String {
        return membreAdapter.toJson(membre)
    }

    // Convertisseur pour Color (stocke comme Int)
    @TypeConverter
    fun colorToInt(color: Color): Long {
        return color.value.toLong()
    }

    @TypeConverter
    fun intToColor(value: Long): Color {
        return Color(value.toULong())
    }
}


@Dao
interface GroupeDao{
    @Query("SELECT * FROM Groupe")
    suspend fun getAll(): List<Groupe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGroupe(groupe: Groupe)

    @Query("DELETE FROM Groupe WHERE id = :id")
    suspend fun deleteGroupe(id: Int)

    @Query("SELECT * FROM Groupe WHERE id = :id")
    suspend fun getGroupeById(id: Int) : Groupe

    @Query("DELETE FROM Groupe")
    suspend fun deleteAll()
}

