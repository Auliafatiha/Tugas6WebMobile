package com.example.twittermbdb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBPostingan(context: Context) :
    SQLiteOpenHelper(context, "PostDB", null, 1) {

        companion object{
            private const val TABLE_NAME = "postingan"
            private const val COLUMN_ID = "id"
            private const val COLUMN_CONTENT = "isi"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """ CREATE TABLE  $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $COLUMN_CONTENT TEXT)""".trimIndent()
        db?.execSQL(createTable)

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertpost(content : String): Long{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, content)
        }

        return db.insert(TABLE_NAME, null, values)
        db.close()

    }

    fun getAllPostingan (): List<String> {
        val  db = readableDatabase
        val list = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val isi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                list.add(isi)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

}