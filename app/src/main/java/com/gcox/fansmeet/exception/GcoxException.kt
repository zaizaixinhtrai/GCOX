package com.gcox.fansmeet.exception

open class GcoxException(
    msg: String = "Unknown error message. Please recheck",
    val code: Int
) : Exception(msg)