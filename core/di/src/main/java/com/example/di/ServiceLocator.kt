package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.mapper.OrganizationModelMapper
import com.example.data.repository.OrganizationRepositoryImpl
import com.example.domain.repository.OrganizationRepository
import com.example.impl.BuildConfigProviderImpl
import com.example.network.DaDataApi
import com.example.network.interceptor.ApiKeyInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java
import com.example.data.repository.OrganizationCacheRepository
import com.example.domain.usecase.GetOrganizationsByQueryUseCase


object ServiceLocator {

    private const val DB_NAME = "organization_cache_db"
    private const val DB_NOT_INITIALIZED_ERROR = "Database is not initialized"

    private var appDatabase: AppDatabase? = null

    private val buildConfigProviderImpl = BuildConfigProviderImpl()

    private val apiKeyInterceptor = ApiKeyInterceptor(buildConfigProviderImpl)

    private val okHttpClient = OkHttpClient.Builder()
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(apiKeyInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(buildConfigProviderImpl.getDaDataApiBaseUrl())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val daDataApi = retrofit.create(DaDataApi::class.java)

    fun getDaDataApi() = daDataApi

    val gson = Gson()

    private val organizationModelMapper = OrganizationModelMapper()

    private val organizationCacheRepository by lazy {
        OrganizationCacheRepository(
            dao = getDatabase().organizationCacheDao(),
            gson = gson,
            ttlMillis = 30_000L
        )
    }

    fun getOrganizationRepository(): OrganizationRepository {
        return OrganizationRepositoryImpl(
            daDataApi = getDaDataApi(),
            organizationModelMapper = organizationModelMapper,
            cacheRepository = organizationCacheRepository
        )
    }

    fun getGetOrganizationsByQueryUseCase(): GetOrganizationsByQueryUseCase {
        return GetOrganizationsByQueryUseCase(
            organizationRepository = getOrganizationRepository()
        )
    }


    fun initDatabase(appCtx: Context) {
        appDatabase = Room.databaseBuilder(
            appCtx,
            AppDatabase::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    fun getDatabase(): AppDatabase =
        appDatabase ?: throw IllegalStateException(DB_NOT_INITIALIZED_ERROR)




}

