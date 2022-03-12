package com.michaelgunawan.cryptolist.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michaelgunawan.cryptolist.model.entity.CurrencyInfo

@Dao
interface CurrencyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg currencyInfo: CurrencyInfo)

    @Query("SELECT * FROM currency_info")
    suspend fun getAll(): List<CurrencyInfo>

}