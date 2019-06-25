package com.streetography.stphotomap.scenes.stphotomap.test.doubles

import com.streetography.stphotomap.operations.base.operations.Operation
import com.streetography.stphotomap.operations.base.operations.OperationQueue

class OperationQueueSpy: OperationQueue() {
    var addOperationCalled: Boolean = false
    var cancelAllOperationsCalled: Boolean = false

    override fun addOperation(operation: Operation) {
        this.addOperationCalled = true
    }

    override fun cancelAllOperations() {
        this.cancelAllOperationsCalled = true
    }
}