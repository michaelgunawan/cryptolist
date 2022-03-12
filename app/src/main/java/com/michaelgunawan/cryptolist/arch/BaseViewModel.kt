package com.michaelgunawan.cryptolist.arch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin

open class BaseViewModel<S : BaseState>(initialState: S) : ViewModel() {

    private val stateFlow = MutableStateFlow(initialState)

    protected val currentState: S
        get() = stateFlow.value

    protected fun setState(block: S.() -> S) {
        val updatedState = block(currentState)
        stateFlow.tryEmit(updatedState)
    }

    fun onStateChanged(): Flow<S> = stateFlow.asStateFlow()

    // DI extension
    inline fun <reified T : Any> ViewModel.inject(
        qualifier: Qualifier? = null,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
        noinline parameters: ParametersDefinition? = null,
    ) = lazy(mode) { get<T>(qualifier, parameters) }

    inline fun <reified T : Any> ViewModel.get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T {
        return getKoin().get(qualifier, parameters)
    }

}