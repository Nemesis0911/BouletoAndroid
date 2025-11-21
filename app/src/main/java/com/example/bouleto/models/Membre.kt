package com.example.bouleto.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


data class Membre(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prenom: String,
    val nom: String,
    var point: List<Point> = listOf(),
){

    val scoreTotal: Int
        get() = point.sumOf { it.score }
}


//@ProvidedTypeConverter
//class ConvertisseurPoint(moshi: Moshi) {
//    private val pointAdapter = moshi.adapter<List<Point>>(
//        Types.newParameterizedType(List::class.java, Point::class.java)
//    )
//
//    @TypeConverter
//    fun stringToPoint(value: String): List<Point>? {
//        return pointAdapter.fromJson(value)
//    }
//
//    @TypeConverter
//    fun pointToString(point: List<Point>): String {
//        return pointAdapter.toJson(point)
//    }
//}
