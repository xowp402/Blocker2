package com.blocker.core.common

/**
 * Validates Android application package names.
 *
 * Package names are used when assigning filtering rules
 * to installed applications.
 */
object AppPackageValidator {

    private val packageNameRegex =
        Regex("^[A-Za-z_][A-Za-z0-9_]*(\\.[A-Za-z_][A-Za-z0-9_]*)+$")

    /**
     * Checks whether a package name has a valid structure.
     */
    fun isValid(packageName: String): Boolean {

        val value = packageName.trim()

        if (value.isEmpty()) return false

        if (value.length > 255) return false

        if (!packageNameRegex.matches(value)) {
            return false
        }

        return true
    }

    /**
     * Normalizes a package name for comparison.
     */
    fun normalize(packageName: String): String? {

        val value = packageName.trim()

        if (!isValid(value)) {
            return null
        }

        return value.lowercase()
    }

    /**
     * Checks whether a package belongs to Blocker itself.
     */
    fun isBlockerPackage(packageName: String): Boolean {

        return normalize(packageName) ==
            BlockerConstants.APPLICATION_ID
    }

    /**
     * Checks whether a package can be added to monitoring.
     */
    fun canMonitor(packageName: String): Boolean {

        val normalized = normalize(packageName)
            ?: return false

        return normalized !=
            BlockerConstants.APPLICATION_ID
    }

    /**
     * Removes invalid and duplicate package names.
     */
    fun filterValid(
        packageNames: Collection<String>
    ): Set<String> {

        return packageNames
            .mapNotNull { normalize(it) }
            .filter { it != BlockerConstants.APPLICATION_ID }
            .toSet()
    }
}
