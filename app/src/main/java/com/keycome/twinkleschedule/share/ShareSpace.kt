package com.keycome.twinkleschedule.share

class ShareSpace<K> {

    private var _isInitialized: Boolean = false

    private var _shareTarget: MutableMap<K, Pair<Target<*>?, Int>>? = null

    public val shareTarget
        get() = if (_isInitialized) _shareTarget!! else {
            _shareTarget = mutableMapOf()
            _isInitialized = true
            _shareTarget!!
        }

    private val releaser: ReleaserImpl<K> = ReleaserImpl()

    @Suppress(names = ["FunctionName"])
    fun <T> ShareOnlyVariable(key: K) = ShareOnlyDelegate<K, T>(key, shareTarget)

    @Suppress(names = ["FunctionName"])
    fun <T> SharePostVariable(key: K, generator: () -> T) =
        SharePostDelegate(key, shareTarget, generator)

    fun releaseReference(key: K) = releaser.releaseReference(key, shareTarget)

    fun releaseAllReference(key: K) = releaser.releaseAllReference(key, shareTarget)

}