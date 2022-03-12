package com.michaelgunawan.cryptolist

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michaelgunawan.cryptolist.di.appModule
import com.michaelgunawan.cryptolist.model.dao.CurrencyInfoDao
import com.michaelgunawan.cryptolist.model.entity.CurrencyInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.lang.RuntimeException

class CryptoApplication : Application() {

    companion object {

        @JvmStatic
        private var instance: CryptoApplication? = null

        private val applicationContext: Context?
            get() = instance?.applicationContext

        fun requireContext(): Context {
            return applicationContext ?: throw RuntimeException("Context is not yet available!")
        }

    }

    private val serializer: Gson by inject()
    private val currencyInfoDao: CurrencyInfoDao by inject()

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@CryptoApplication)
            modules(appModule)
        }

        // Prepopulate test DB
        val streamReader = requireContext().resources.assets
            .open("currency_info_data.json")
            .reader()
        val listType = object : TypeToken<List<CurrencyInfo>>(){}.type
        val list = serializer.fromJson<List<CurrencyInfo>>(streamReader, listType)
        GlobalScope.launch {
            currencyInfoDao.insert(*list.toTypedArray())
        }
    }

}