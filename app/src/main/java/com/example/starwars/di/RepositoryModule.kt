package com.example.starwars.di

import com.example.starwars.data.repository.PlayerRepositoryImpl
import com.example.starwars.domain.repository.PlayerRepository
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
    abstract fun bindPlayerRepository(
        playerRepository: PlayerRepositoryImpl
    ): PlayerRepository
}