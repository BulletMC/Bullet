package com.aznos.util

import java.io.File

/**
 * Utility to detect if a folder is an Anvil world format.
 */
object WorldFormatDetector {
    /**
     * Checks if the given folder is an Anvil world format.
     *
     * @param folder The folder to check.
     * @return True if the folder is an Anvil world format, false otherwise.
     */
    fun isAnvilRoot(folder: File): Boolean = File(folder, "level.dat").isFile
            && File(folder, "region").isDirectory
}