package com.streetography.stphotomap.operations.base.operations

open class OperationQueue {
    private var operations: ArrayList<Operation> = ArrayList()
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