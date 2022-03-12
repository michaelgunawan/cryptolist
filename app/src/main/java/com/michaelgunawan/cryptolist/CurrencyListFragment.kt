package com.michaelgunawan.cryptolist

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.michaelgunawan.cryptolist.arch.BaseFragment
import com.michaelgunawan.cryptolist.arch.ViewBindingRecyclerAdapter
import com.michaelgunawan.cryptolist.databinding.FragmentCurrencyListBinding
import com.michaelgunawan.cryptolist.databinding.RowCurrencyInfoBinding
import com.michaelgunawan.cryptolist.model.entity.CurrencyInfo
import com.michaelgunawan.cryptolist.view.tapped
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CurrencyListFragment : BaseFragment<FragmentCurrencyListBinding>() {

    override val layoutId: Int = R.layout.fragment_currency_list
    private val viewModel: DemoViewModel by activityViewModels()
    private val adapter = ViewBindingRecyclerAdapter()

    override fun onViewCreated(binding: FragmentCurrencyListBinding, savedInstanceState: Bundle?) {
        super.onViewCreated(binding, savedInstanceState)

        // Setup adapter
        // We can add decorator here if we need separator between items
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Handle item population
        viewModel.onStateChanged()
            .mapNotNull { it.currencyInfoList }
            .distinctUntilChanged()
            .consume { currencyInfoList ->
                adapter.updateItems(currencyInfoList.map { currencyInfo ->
                    CurrencyInfoAdapterItem(currencyInfo)
                })
            }

        // Handle user interaction
        adapter.addItemActionHandler { itemBinding: RowCurrencyInfoBinding, item: CurrencyInfoAdapterItem ->
            itemBinding.root.tapped()
                .consume {
                    viewModel.currencyTapped(item.currencyInfo)
                }
        }
    }

}