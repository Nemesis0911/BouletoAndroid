package com.example.bouleto.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bouleto.models.Convertisseur
import com.example.bouleto.models.Groupe
import com.example.bouleto.models.GroupeDao



@Database(entities = [Groupe::class], version = 1, exportSchema = false)
@TypeConverters(Convertisseur::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupeDao(): GroupeDao
}