package com.streetography.stphotomap.operations.base.operations

class OperationQueue {
    private var operations: ArrayList<Operation> = ArrayList()

    fun addOperation(operation: Operation) {
        this.operations.add(operation)
        operation.run {
            this.operations.remove(operation)
        }
    }

    fun cancelAllOperations() {
        this.operations.forEach { it.cancel() }
        this.operations.clear()
    }
}