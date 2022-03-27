package com.keycome.twinkleschedule.delivery

class GetBoundScopeImpl(private val direction: String) : GetBoundScope {

    override fun getLong(title: String, default: Long): Long {
        return PostOffice.getLongBound(direction, title)?.content ?: default
    }

    override fun getInt(title: String, default: Int): Int {
        return PostOffice.getIntBound(direction, title)?.content ?: default
    }

    override fun getString(title: String, default: String): String {
        return PostOffice.getStringBound(direction, title)?.content ?: default
    }
}