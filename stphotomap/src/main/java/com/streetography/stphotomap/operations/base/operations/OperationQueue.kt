package com.streetography.stphotomap.operations.base.operations

import java.util.concurrent.CopyOnWriteArrayList

open class OperationQueue {
    private var operations: CopyOnWriteArrayList<Operation> = CopyOnWriteArrayList()
    var operationCount = this.operations.size

    open fun addOperation(operation: Operation) {
        this.operations.add(operation)
        operation.run {
            this.operations.remove(operation)
        }
    }

    open fun cancelAllOperations() {
        this.operations.forEach { it.cancel() }
        this.operations.clear()
    }
}