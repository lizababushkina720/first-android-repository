package com.example.data.di


import com.example.data.repository.OrganizationRepositoryImpl
import com.example.domain.repository.OrganizationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindOrganizationRepository(
        impl: OrganizationRepositoryImpl
    ): OrganizationRepository
}