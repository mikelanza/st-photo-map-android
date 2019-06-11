package com.streetography.stphotomap.operations.base.results

import com.streetography.stphotomap.operations.base.errors.OperationError

interface OperationResult<T> {
    fun onSuccess(value: T)
    fun onFailure(error: OperationError)
}