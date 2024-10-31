package com.bgbrlk.scoreboardbrlk.di

import android.content.Context
import com.bgbrlk.scoreboardbrlk.repository.AppDatastoreInterface
import com.bgbrlk.scoreboardbrlk.repository.AppDatastoreRepository
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
    fun providesAppDatastoreRepository(@ApplicationContext context: Context): AppDatastoreInterface =
        AppDatastoreRepository(context)
}