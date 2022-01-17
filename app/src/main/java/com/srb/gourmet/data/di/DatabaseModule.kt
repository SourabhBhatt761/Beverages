package com.srb.gourmet.data.di

import android.content.Context
import androidx.room.Room
import com.srb.gourmet.data.database.RecipesDatabase
import com.srb.gourmet.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration()          //used when we change the version and want to delete the data for the prev. one
        .build()

    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase) = database.recipesDao()

}