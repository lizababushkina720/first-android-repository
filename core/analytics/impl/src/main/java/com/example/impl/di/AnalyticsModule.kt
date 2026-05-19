package com.example.impl.di

import com.example.api.AboutAppAnalytics
import com.example.api.Analytics
import com.example.api.OrganizationRequestAnalytics
import com.example.impl.AboutAppAnalyticsImpl
import com.example.impl.FirebaseAnalyticsImpl
import com.example.impl.OrganizationRequestAnalyticsImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AnalyticsModule {

    @Binds
    @Singleton
    fun bindAnalyticsToImpl(impl: FirebaseAnalyticsImpl): Analytics

    @Binds
    @Singleton
    fun bindOrganizationRequestAnalyticsToImpl(impl: OrganizationRequestAnalyticsImpl): OrganizationRequestAnalytics

    @Binds
    @Singleton
    fun bindAboutAppAnalytics(impl: AboutAppAnalyticsImpl): AboutAppAnalytics
}