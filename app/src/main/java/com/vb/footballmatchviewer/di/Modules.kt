package com.vb.footballmatchviewer.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vb.footballmatchviewer.data.local.database.FootballDatabase
import com.vb.footballmatchviewer.data.local.model.SettingsEntity
import com.vb.footballmatchviewer.data.local.repository.FootballDatabaseRepository
import com.vb.footballmatchviewer.data.local.repository.FootballDatabaseRepositoryImpl
import com.vb.footballmatchviewer.data.remote.interceptor.FootballInterceptor
import com.vb.footballmatchviewer.data.remote.api.FootballService
import com.vb.footballmatchviewer.data.remote.repository.FootballNetworkRepository
import com.vb.footballmatchviewer.data.remote.repository.FootballNetworkRepositoryImpl
import com.vb.footballmatchviewer.ui.main.viewmodel.MainViewModel
import com.vb.footballmatchviewer.ui.main.viewmodel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val mainModule = module {
    single<FootballDatabase> {
        lateinit var instance: FootballDatabase

        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    instance.settingsDao().saveSettings(
                        SettingsEntity(
                            id = 0,
                            isDarkTheme = true
                        )
                    )
                }
            }
        }

        instance = Room.databaseBuilder(
            androidContext(),
            FootballDatabase::class.java,
            "football_database"
        )
            .addCallback(callback)
            .build()

        instance
    }
    single<FootballDatabaseRepository> { FootballDatabaseRepositoryImpl(get(FootballDatabase::class.java)) }
    single<FootballService> {
        val json = Json {
            ignoreUnknownKeys = true
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(FootballInterceptor())
            .build()

        Retrofit.Builder()
            .baseUrl("https://v3.football.api-sports.io/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
            .create(FootballService::class.java)
    }
    single<FootballNetworkRepository> { FootballNetworkRepositoryImpl(get(FootballService::class.java)) }
    viewModel<MainViewModel> {
        MainViewModel(
            get(FootballDatabaseRepository::class.java),
            get(FootballNetworkRepository::class.java)
        )
    }
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            get(FootballDatabaseRepository::class.java),
        )
    }
}
