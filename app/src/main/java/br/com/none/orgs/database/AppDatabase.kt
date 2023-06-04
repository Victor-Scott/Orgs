package br.com.none.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.none.orgs.database.converters.Converters
import br.com.none.orgs.database.dao.ProductDao
import br.com.none.orgs.database.dao.UserDao
import br.com.none.orgs.model.Product
import br.com.none.orgs.model.User

@Database(
    entities = [Product::class,
        User::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        fun instance(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
            )
                .build()
                .also { db = it }
        }
    }
}