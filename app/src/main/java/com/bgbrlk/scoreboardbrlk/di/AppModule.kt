package com.bgbrlk.scoreboardbrlk.di

import android.content.Context
import com.bgbrlk.scoreboardbrlk.data.DatastoreHelper
import com.bgbrlk.scoreboardbrlk.data.remote.AdminSettings
import com.bgbrlk.scoreboardbrlk.data.local.LocalSettings
import com.bgbrlk.scoreboardbrlk.data.remote.FirebaseAdminSettings
import com.bgbrlk.scoreboardbrlk.domain.repository.AppSettings
import com.bgbrlk.scoreboardbrlk.domain.repository.AppSettingsRepository
import com.bgbrlk.scoreboardbrlk.data.local.DatastoreLocalSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesAppSettings(
        datastoreLocalSettings: LocalSettings,
        firebaseAdminSettings: AdminSettings
    ): AppSettings = AppSettingsRepository(datastoreLocalSettings, firebaseAdminSettings)

    @Singleton
    @Provides
    fun providesFirebaseAdminSettings(): AdminSettings = FirebaseAdminSettings()

    @Singleton
    @Provides
    fun providesDatastoreLocalSettings(
        datastoreHelper: DatastoreHelper
    ): LocalSettings = DatastoreLocalSettings(datastoreHelper)

    @Singleton
    @Provides
    fun providesDatastoreHelper(@ApplicationContext context: Context): DatastoreHelper = DatastoreHelper(context)
}