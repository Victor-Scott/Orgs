package br.com.alura.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alura.orgs.database.converters.Converters
import br.com.alura.orgs.database.dao.ProductDao
import br.com.alura.orgs.database.dao.UserDao
import br.com.alura.orgs.model.Product
import br.com.alura.orgs.model.User

@Database(
    entities = [Product::class,
                    User::class],
    version = 2,
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
            ).addMigrations(MIGRATION_1_2)
                .build()
                .also { db = it }
        }
    }
}