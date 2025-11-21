package com.example.bouleto.repository

import android.content.Context
import androidx.room.Room
import com.example.bouleto.database.AppDatabase
import com.example.bouleto.models.ConvertisseurMembre

import com.example.bouleto.models.Groupe
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class BddRepository(context: Context) {
    val database = Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
        .addTypeConverter(
            ConvertisseurMembre(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build()


    val dao = database.groupeDao()

    suspend fun getAll() = dao.getAll()

    suspend fun addGroupe(groupe: Groupe) = dao.addGroupe(groupe)

    suspend fun deleteGroupe(id: Int) = dao.deleteGroupe(id)


    suspend fun getGroupeById(id : Int) : Groupe = dao.getGroupeById(id)
    suspend fun deleteAll() = dao.deleteAll()

    suspend fun updateGroupe(groupe: Groupe) {
        dao.updateGroupe(groupe)
    }

}