package com.streetography.stphotomap.operations.base.operations

import java.util.*

open class Operation {
    val id: String = UUID.randomUUID().toString()

    protected var isLoggingEnabled: Boolean = false
    var isCancelled: Boolean = false
    var completion: (() -> Unit)? = null

    open fun run(completion: (() -> Unit)?) {
        this.completion = completion
    }

    open fun cancel() {
        this.isCancelled = true
    }

    open fun log() {
        if (!this.isLoggingEnabled) return
    }

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != this.javaClass) return false

        other as Operation
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}