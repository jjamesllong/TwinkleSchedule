package com.keycome.twinkleschedule.extension

import android.database.Observable
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.Adapter<*>.removeObservers() {
    var clazz: Class<*> = javaClass
    val simpleName = RecyclerView.Adapter::class.simpleName
    while (clazz.simpleName != simpleName) {
        clazz = clazz.superclass
    }
    val field = clazz.getDeclaredField("mObservable")
    field.isAccessible = true
    val observable = field[this] as Observable<*>
    observable.unregisterAll()
}