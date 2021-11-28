package test.com.sampleapp.mor.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.com.sampleapp.mor.data.cache.AppDatabase
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
    @Singleton
    @DatabaseUseInMemoryFlag
    fun provideUseInMemoryFlag(): Boolean = false
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseUseInMemoryFlag