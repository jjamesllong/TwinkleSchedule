package com.keycome.twinkleschedule.base

import com.keycome.twinkleschedule.share.ShareSpace

abstract class Pipette {

    companion object {

        val pipettes = ShareSpace<String>()

        inline fun <reified P : Pipette> pipettes() = pipettes.SharePostVariable(
            key = P::class.simpleName ?: throw Exception()
        ) { P::class.java.newInstance() }

        fun releasePipettes(key: String) = pipettes.releaseAllReference(key)
    }
}

