package com.michaelgunawan.cryptolist

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.michaelgunawan.cryptolist.model.Sorting
import com.michaelgunawan.cryptolist.view.tapped
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DemoActivity : AppCompatActivity() {

    private val viewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // View references
        val generateButton = findViewById<Button>(R.id.button_generate)
        val sortButton = findViewById<Button>(R.id.button_sort)

        // View input handling
        generateButton.tapped()
            .onEach {
                viewModel.generateData()
            }
            .launchIn(lifecycleScope)

        sortButton.tapped()
            .onEach {
                viewModel.triggerSorting()
            }
            .launchIn(lifecycleScope)

        // State changes handler
        viewModel.onStateChanged()
            .distinctUntilChanged()
            .onEach { state ->
                generateButton.isEnabled = state.currencyInfoList.isNullOrEmpty() && !state.isLoading
                // We don't allow sorting when data does not exist or is processing
                sortButton.isEnabled = !state.currencyInfoList.isNullOrEmpty() && !state.isLoading
                generateButton.text = getString(
                    when (state.isLoading && state.currencyInfoList.isNullOrEmpty()) {
                        true -> R.string.demo_screen_button_generating
                        false -> R.string.demo_screen_button_generate
                    }
                )
                sortButton.text = getString(
                    when (state.sortingState) {
                        null -> R.string.demo_screen_button_sort
                        Sorting.ASCENDING -> R.string.demo_screen_button_sort_asc
                        Sorting.DESCENDING -> R.string.demo_screen_button_sort_desc
                    }
                )
            }
            .launchIn(lifecycleScope)

        // Handle crypto list tapped
        viewModel.onCurrencyTapped()
            .onEach { currencyInfo ->
                Toast.makeText(this, "${currencyInfo.name} tapped!", Toast.LENGTH_SHORT).show()
            }
            .launchIn(lifecycleScope)
    }

}