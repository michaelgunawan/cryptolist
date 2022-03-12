package com.michaelgunawan.cryptolist.di

import androidx.room.Room
import com.google.gson.Gson
import com.michaelgunawan.cryptolist.model.AppDatabase
import com.michaelgunawan.cryptolist.model.dao.CurrencyInfoDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            this.androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    factory<CurrencyInfoDao> {
        get<AppDatabase>().currencyInfoDao()
    }

    single<Gson> {
        Gson()
    }

}