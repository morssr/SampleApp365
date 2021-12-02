package test.com.sampleapp.mor.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.com.sampleapp.mor.data.PropertiesRepository
import test.com.sampleapp.mor.data.Repository
import test.com.sampleapp.mor.data.api.PropertiesService
import test.com.sampleapp.mor.data.cache.AppDatabase
import test.com.sampleapp.mor.data.cache.FLAG_USE_IN_MEMORY_DB
import test.com.sampleapp.mor.data.cache.PropertiesDao
import test.com.sampleapp.mor.data.cache.TenantsDao
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @DatabaseUseInMemoryFlag useInMemory: Boolean
    ): AppDatabase {
        return AppDatabase.getInstance(context, useInMemory)
    }

    @Provides
    fun providePropertiesDao(database: AppDatabase): PropertiesDao {
        return database.propertiesDao()
    }

    @Provides
    fun trendingTenantsDao(database: AppDatabase): TenantsDao {
        return database.tenantsDao()
    }

    @Provides
    fun provideRepository(
        database: AppDatabase,
        propertiesService: PropertiesService
    ): PropertiesRepository {
        return Repository(propertiesService, database)
    }

    @Provides
    @Singleton
    @DatabaseUseInMemoryFlag
    fun provideUseInMemoryFlag(): Boolean = FLAG_USE_IN_MEMORY_DB
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseUseInMemoryFlag