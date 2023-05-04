package br.com.alura.orgs.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `User` (
            `uid` TEXT NOT NULL, 
            `name` TEXT NOT NULL, 
            `password` TEXT NOT NULL, PRIMARY KEY(`uid`)
            )""")

    }

}