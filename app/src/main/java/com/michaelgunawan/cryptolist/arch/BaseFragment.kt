package com.michaelgunawan.cryptolist.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {

    abstract val layoutId: Int
    private lateinit var binding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(binding, savedInstanceState)
    }

    open fun onViewCreated(binding: VDB, savedInstanceState: Bundle?) {}

    // Convenience functions
    protected fun <T : Any?> Flow<T>.consume(block: (T) -> Unit) {
        this.onEach { block(it) }.launchIn(lifecycleScope)
    }

}