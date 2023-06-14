package com.example.stockmarketkotlin.di

import com.example.stockmarketkotlin.data.repository.AuthRepository
import com.example.stockmarketkotlin.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ):AuthRepository
}