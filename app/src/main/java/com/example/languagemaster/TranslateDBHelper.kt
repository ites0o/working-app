package com.example.languagemaster

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TranslateDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 2 // Incremented to trigger onUpgrade()
        const val DATABASE_NAME = "translate_history.db"
        const val TABLE_NAME = "history"
        const val COLUMN_ID = "_id"
        const val COLUMN_USER_ID = "user_id" // Added column
        const val COLUMN_INPUT = "input"
        const val COLUMN_OUTPUT = "output"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_USER_ID TEXT, $COLUMN_INPUT TEXT, $COLUMN_OUTPUT TEXT, $COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}


