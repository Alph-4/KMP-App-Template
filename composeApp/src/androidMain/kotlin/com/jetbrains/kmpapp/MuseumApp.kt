package com.jetbrains.kmpapp

import android.app.Application
import androidx.room.Room
import com.jetbrains.kmpapp.data.local.AppDatabase
import com.jetbrains.kmpapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class MuseumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            // C'est ici qu'on injecte le contexte Android
            androidContext(this@MuseumApp)

            // On ajoute le module spécifique à Android (Database)
            modules(
                module {
                    single {
                        Room.databaseBuilder(
                            this@MuseumApp,
                            AppDatabase::class.java,
                            "museum-database"
                        )
                            .fallbackToDestructiveMigration() // Utile en dev si on change le schéma
                            .build()
                    }
                }
            )
        }
    }
}
