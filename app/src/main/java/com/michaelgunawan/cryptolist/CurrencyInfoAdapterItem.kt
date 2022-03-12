package com.michaelgunawan.cryptolist

import com.michaelgunawan.cryptolist.arch.AdapterItem
import com.michaelgunawan.cryptolist.arch.ViewBindingHolder
import com.michaelgunawan.cryptolist.databinding.RowCurrencyInfoBinding
import com.michaelgunawan.cryptolist.model.entity.CurrencyInfo

class CurrencyInfoAdapterItem(val currencyInfo: CurrencyInfo) : AdapterItem<RowCurrencyInfoBinding> {
    override val layoutId: Int = R.layout.row_currency_info
    override val id: Long = currencyInfo.id.hashCode().toLong()

    override fun bindItem(
        binding: RowCurrencyInfoBinding,
        holder: ViewBindingHolder<RowCurrencyInfoBinding>
    ) {
        binding.icon.text = currencyInfo.name.take(1)
        binding.name.text = currencyInfo.name
        binding.symbol.text = currencyInfo.symbol
    }
}