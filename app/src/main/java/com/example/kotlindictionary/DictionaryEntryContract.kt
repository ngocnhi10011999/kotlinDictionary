package com.example.kotlindictionary

import android.provider.BaseColumns

class DictionaryEntryContract : BaseColumns {
    private constructor()

    companion object {
        val TABLE_NAME = "av"

        val COLUMN_ID = BaseColumns._ID
        val COLUMN_WORD = "word"
        val COLUMN_TYPE = "description"
        val COLUMN_MEANING = "pronounce"
    }
}