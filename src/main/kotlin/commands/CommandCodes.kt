package com.aznos.commands

/**
 * Enum class that contains the command codes that can be sent to the client
 */
enum class CommandCodes {
    UNKNOWN,
    SUCCESS,
    ILLEGAL_SYNTAX,
    ILLEGAL_ARGUMENT,
    INVALID_PERMISSIONS,
    ;

    val id: Int
        get() = ordinal

}