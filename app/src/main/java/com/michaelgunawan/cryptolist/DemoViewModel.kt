package com.michaelgunawan.cryptolist

import com.michaelgunawan.cryptolist.arch.BaseState
import com.michaelgunawan.cryptolist.arch.BaseViewModel
import com.michaelgunawan.cryptolist.arch.LoadingState
import com.michaelgunawan.cryptolist.model.Sorting
import com.michaelgunawan.cryptolist.model.dao.CurrencyInfoDao
import com.michaelgunawan.cryptolist.model.entity.CurrencyInfo
import com.michaelgunawan.cryptolist.model.reverse
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DemoViewModel : BaseViewModel<DemoViewModel.DemoState>(DemoState()) {

    data class DemoState(
        val currencyInfoList: List<CurrencyInfo>? = null,
        val sortingState: Sorting? = null,
        override val isLoading: Boolean = false
    ) : BaseState, LoadingState

    private val currencyInfoDao: CurrencyInfoDao by inject()
    private val currencyInfoInteraction = MutableSharedFlow<CurrencyInfo>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    suspend fun generateData() {
        setState { copy(isLoading = true) }
        // delay(1000) // Artificial processing time to simulate slow connection or huge dataset
        val currencyList = currencyInfoDao.getAll()

        setState { copy(currencyInfoList = currencyList, isLoading = false) }
    }

    suspend fun triggerSorting() {
        val targetSortingState = currentState.sortingState.reverse()
        setState { copy(sortingState = targetSortingState, isLoading = true) }
        // delay(1000) // Artificial processing time to simulate slow connection or huge dataset
        sortData(targetSortingState)
        setState { copy(isLoading = false) }
    }

    private fun sortData(sorting: Sorting) {
        val currentInfoList = currentState.currencyInfoList
        val targetInfoList = when (sorting) {
            Sorting.ASCENDING -> {
                currentInfoList?.sortedBy { it.name }
            }
            Sorting.DESCENDING -> {
                currentInfoList?.sortedByDescending { it.name }
            }
        }
        setState { copy(currencyInfoList = targetInfoList) }
    }

    fun currencyTapped(currencyInfo: CurrencyInfo) {
        currencyInfoInteraction.tryEmit(currencyInfo)
    }

    // Listener for when list is tapped
    fun onCurrencyTapped(): Flow<CurrencyInfo> {
        return currencyInfoInteraction.asSharedFlow()
    }

}