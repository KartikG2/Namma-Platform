package com.namma.platform.di

import com.namma.platform.data.repository.TrainRepositoryImpl
import com.namma.platform.domain.repository.TrainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTrainRepository(
        trainRepositoryImpl: TrainRepositoryImpl
    ): TrainRepository
}
