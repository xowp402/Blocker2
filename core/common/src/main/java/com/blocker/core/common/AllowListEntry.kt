package com.blocker.core.common

/**
 * An exception that allows a selected app or website.
 *
 * Allow-list entries are evaluated after block rules.
 * A matching active block rule always takes priority.
 */
data class AllowListEntry(

    val id: String,

    val value: String,

    val type: AllowListType,

    val enabled: Boolean = true,

    val includeSubdomains: Boolean = false,

    val targetPackage: String? = null,

    val note: String? = null,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(id.isNotBlank()) {
            "Allow-list ID cannot be empty."
        }

        require(value.isNotBlank()) {
            "Allow-list value cannot be empty."
        }

        if (type == AllowListType.APP) {
            require(targetPackage == null) {
                "An app exception must not have a target package."
            }
        }
    }

    /**
     * Checks whether this entry matches an application.
     */
    fun matchesApp(packageName: String): Boolean {

        if (!enabled || type != AllowListType.APP) {
            return false
        }

        return value.equals(
            packageName,
            ignoreCase = true
        )
    }

    /**
     * Checks whether this entry matches a website hostname.
     */
    fun matchesWebsite(hostname: String): Boolean {

        if (!enabled || type != AllowListType.WEBSITE) {
            return false
        }

        val host = normalizeDomain(hostname)
        val allowed = normalizeDomain(value)

        if (host.isBlank() || allowed.isBlank()) {
            return false
        }

        if (host == allowed) {
            return true
        }

        return includeSubdomains &&
            host.endsWith(".$allowed")
    }

    private fun normalizeDomain(domain: String): String {

        return domain
            .trim()
            .lowercase()
            .removePrefix("https://")
            .removePrefix("http://")
            .substringBefore("/")
            .substringBefore("?")
            .substringBefore("#")
            .substringBefore(":")
            .trimEnd('.')
    }
}

enum class AllowListType {
    APP,
    WEBSITE
}
