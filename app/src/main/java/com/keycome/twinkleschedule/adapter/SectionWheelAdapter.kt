package com.keycome.twinkleschedule.adapter

import com.contrarywind.adapter.WheelAdapter

class SectionWheelAdapter : WheelAdapter<Int> {

    val list = (1..16).toList()

    override fun getItemsCount(): Int {
        return list.size
    }

    override fun getItem(index: Int): Int {
        return list[index]
    }

    override fun indexOf(o: Int?): Int {
        return list.indexOf(o)
    }
}