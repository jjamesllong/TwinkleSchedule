package com.keycome.twinkleschedule.delivery

class PutBoundScopeImpl(private val direction: String) : PutBoundScope {

    override fun putLong(title: String, content: Long) {
        PostOffice.putLongBound(Bound(direction, title, content))
    }

    override fun putInt(title: String, content: Int) {
        PostOffice.putIntBound(Bound(direction, title, content))
    }

    override fun putString(title: String, content: String) {
        PostOffice.putStringBound(Bound(direction, title, content))
    }
}