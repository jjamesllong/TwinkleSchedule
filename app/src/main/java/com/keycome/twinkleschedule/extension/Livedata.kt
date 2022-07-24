package com.keycome.twinkleschedule.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun <I, O> LiveData<I>.map(block: (I) -> O): LiveData<O> =
    Transformations.map(this, block)