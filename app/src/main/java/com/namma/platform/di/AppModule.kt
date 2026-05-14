package com.namma.platform.di

import android.content.Context
import androidx.room.Room
import com.namma.platform.data.local.dao.StationDao
import com.namma.platform.data.local.dao.TrainDao
import com.namma.platform.data.local.db.NammaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ─── Database ───

    @Provides
    @Singleton
    fun provideNammaDatabase(
        @ApplicationContext context: Context
    ): NammaDatabase {
        return Room.databaseBuilder(
            context,
            NammaDatabase::class.java,
            NammaDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideStationDao(db: NammaDatabase): StationDao {
        return db.stationDao()
    }

    @Provides
    @Singleton
    fun provideTrainDao(db: NammaDatabase): TrainDao {
        return db.trainDao()
    }
}
