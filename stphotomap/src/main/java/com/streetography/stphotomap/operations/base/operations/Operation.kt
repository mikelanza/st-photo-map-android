package com.streetography.stphotomap.operations.base.operations

open class Operation {
    protected var isLoggingEnabled: Boolean = false
    var isCancelled: Boolean = false

    open fun run() {

    }

    open fun cancel() {
        isCancelled = true
    }

    open fun log() {
        if (!isLoggingEnabled) return
    }
}