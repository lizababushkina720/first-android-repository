package com.example.impl.di


import com.example.api.BuildConfigProvider
import com.example.impl.BuildConfigProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BuildConfigModule {
    @Binds
    @Singleton
    fun bindBuildConfigProvider(
        impl: BuildConfigProviderImpl
    ): BuildConfigProvider
}