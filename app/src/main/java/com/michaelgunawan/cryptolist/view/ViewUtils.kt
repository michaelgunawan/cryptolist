package com.michaelgunawan.cryptolist.view

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

fun View.tapped(): Flow<View> = callbackFlow {
    this@tapped.setOnClickListener { button ->
        trySend(button)
    }

    awaitClose {
        this@tapped.setOnClickListener(null)
    }
}.debounce(200) // To handle all multi tap on single button usecases