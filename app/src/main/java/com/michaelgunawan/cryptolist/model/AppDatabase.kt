package com.michaelgunawan.cryptolist.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.michaelgunawan.cryptolist.model.dao.CurrencyInfoDao
import com.michaelgunawan.cryptolist.model.entity.CurrencyInfo

@Database(entities = [CurrencyInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyInfoDao(): CurrencyInfoDao
}