package com.example.t9search.feature_search

import android.content.Context
import com.example.t9search.DataRepository
import com.example.t9search.datastore.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesDataRepository(
        @ApplicationContext context: Context
    ) = DataRepository(context)

    @Provides
    @Singleton
    fun providesDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context)

}