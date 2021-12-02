package test.com.sampleapp.mor.data.cache

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import test.com.sampleapp.mor.data.cache.entities.Property
import test.com.sampleapp.mor.data.cache.entities.Tenant
import test.com.sampleapp.mor.data.cache.paging.RemoteKeys
import test.com.sampleapp.mor.data.cache.paging.RemoteKeysDao
import test.com.sampleapp.mor.data.cache.utilities.converters.DateTypeConverter
import test.com.sampleapp.mor.data.cache.utilities.converters.EntitiesConverters

const val FLAG_USE_IN_MEMORY_DB = false

@TypeConverters(DateTypeConverter::class, EntitiesConverters::class)
@Database(
    entities = [Property::class, Tenant::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun propertiesDao(): PropertiesDao
    abstract fun tenantsDao(): TenantsDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        private const val TAG = "AppDatabase"
        private const val DATABASE_NAME = "mor365.db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, useInMemory: Boolean = FLAG_USE_IN_MEMORY_DB): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context, useInMemory).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, useInMemory: Boolean): AppDatabase {
            val databaseBuilder =
                if (useInMemory) Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                    .addCallback(
                        object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d(TAG, "onCreate: Im memory Database created successfully")
                            }
                        }
                    )
                else Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(
                        object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d(TAG, "onCreate:Persistent Database created successfully")
                            }
                        }
                    )

            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}