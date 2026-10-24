package com.phantom.killer.disabler.domain.model

class ExecutionResult(
    val isSuccess: Boolean,
    val errorCode: AdbErrorCode = if (isSuccess) AdbErrorCode.NONE else AdbErrorCode.UNKNOWN_ERROR,
    val message: String = "",
    val output: String = ""
) {
    companion object {
        @JvmStatic
        fun success(output: String = ""): ExecutionResult =
            ExecutionResult(true, AdbErrorCode.NONE, output, output)

        @JvmStatic
        fun failure(message: String): ExecutionResult =
            ExecutionResult(false, AdbErrorCode.UNKNOWN_ERROR, message, "")
    }

    val errorMessage: String
        get() = if (!isSuccess) message else ""
}
