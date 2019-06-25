package com.streetography.stphotomap.operations.base.errors

enum class OperationError(val value: Int) {
    NO_URL_AVAILABLE(1),
    NO_DATA_AVAILABLE(2),
    CANNOT_PARSE_RESPONSE(3),
    NO_INTERNET_CONNECTION(4),
    NO_CONTEXT_AVAILABLE(5),
    OPERATION_CANCELLED(6);

    val errorCode: Int
        get() = value

    val errorMessage: String?
        get() {
            when (this) {
                NO_URL_AVAILABLE -> return "No url available."
                NO_DATA_AVAILABLE -> return "No data available."
                CANNOT_PARSE_RESPONSE -> return "Cannot parse response."
                NO_INTERNET_CONNECTION -> return "No internet connection."
                NO_CONTEXT_AVAILABLE -> return "No context available."
                OPERATION_CANCELLED -> return "Operation cancelled"
            }
        }

    companion object {
        fun fromInt(i: Int): OperationError? {
            for (b in values()) {
                if (b.value == i) {
                    return b
                }
            }
            return null
        }
    }
}