package com.example.bouleto.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bouleto.models.ConvertisseurMembre

import com.example.bouleto.models.Groupe
import com.example.bouleto.models.GroupeDao
import com.example.bouleto.models.Membre
import com.example.bouleto.models.Point

@Database(entities = [Groupe::class], version = 1)
@TypeConverters(ConvertisseurMembre::class )
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupeDao(): GroupeDao
}