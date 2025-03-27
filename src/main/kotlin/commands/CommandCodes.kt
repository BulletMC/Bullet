package com.aznos.commands

/**
 * Enum class that contains the command codes that can be sent to the client
 */
enum class CommandCodes(val id: Int) {
    UNKNOWN(0),
    SUCCESS(1),
    ILLEGAL_SYNTAX(2),
    ILLEGAL_ARGUMENT(3),
    INVALID_PERMISSIONS(4)
}