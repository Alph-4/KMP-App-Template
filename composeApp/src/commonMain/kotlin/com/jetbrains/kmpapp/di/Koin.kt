package com.jetbrains.kmpapp.di

import com.jetbrains.kmpapp.data.local.AppDatabase
import com.jetbrains.kmpapp.data.remote.KtorMistralApi
import com.jetbrains.kmpapp.data.remote.KtorMuseumApi
import com.jetbrains.kmpapp.data.remote.MistralApi
import com.jetbrains.kmpapp.data.remote.MuseumApi
import com.jetbrains.kmpapp.repository.AiRepository
import com.jetbrains.kmpapp.repository.MuseumRepository
import com.jetbrains.kmpapp.viewmodel.DetailViewModel
import com.jetbrains.kmpapp.viewmodel.ListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }
    single<MuseumApi> { KtorMuseumApi(get()) }
    single<MistralApi> { KtorMistralApi(get()) }
    single { get<AppDatabase>().museumDao() }

    single {
        MuseumRepository(get(), get()).apply {
            initialize()
        }

    }
    single {
        AiRepository(get())
    }
}

val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
