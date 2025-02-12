package com.bgbrlk.scoreboardbrlk.di

import android.content.Context
import com.bgbrlk.scoreboardbrlk.repository.AppDatastore
import com.bgbrlk.scoreboardbrlk.repository.AppDatastoreInterface
import com.bgbrlk.scoreboardbrlk.repository.AppRemoteConfig
import com.bgbrlk.scoreboardbrlk.repository.AppRules
import com.bgbrlk.scoreboardbrlk.repository.AppRulesRepository
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
    fun providesAppRulesRepository(appDatastore: AppDatastoreInterface, remoteConfig: AppRemoteConfig): AppRules =
        AppRulesRepository(appDatastore, remoteConfig)

    @Singleton
    @Provides
    fun providesDatastoreRepository(@ApplicationContext context: Context): AppDatastoreInterface = AppDatastore(context)

    @Singleton
    @Provides
    fun providesAppRemoteConfig(): AppRemoteConfig = AppRemoteConfig()
}