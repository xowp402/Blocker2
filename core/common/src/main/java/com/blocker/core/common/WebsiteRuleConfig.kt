package com.blocker.core.common

/**
 * Configuration for an individual website-blocking rule.
 */
data class WebsiteRuleConfig(

    val id: String,

    val domain: String,

    val enabled: Boolean = true,

    val includeSubdomains: Boolean = true,

    val targetBrowsers: Set<String> = emptySet(),

    val scope: RuleScope = RuleScope.ALL_BROWSERS,

    val blockDurationSeconds: Long =
        BlockerConstants.DEFAULT_BLOCK_DURATION_SECONDS,

    val permanentBlock: Boolean = false,

    val warningMessage: String =
        "این وب‌سایت طبق قوانین Blocker موقتاً مسدود شده است.",

    val relatedDomains: Set<String> = emptySet(),

    val detectionCount: Long = 0L,

    val lastBlockedAt: Long? = null,

    val priority: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),

    val updatedAt: Long = System.currentTimeMillis()
) {

    init {
        require(id.isNotBlank()) {
            "Rule ID cannot be empty."
        }

        require(domain.isNotBlank()) {
            "Domain cannot be empty."
        }

        require(
            permanentBlock ||
                blockDurationSeconds in
                BlockerConstants.MIN_BLOCK_DURATION_SECONDS..
                BlockerConstants.MAX_BLOCK_DURATION_SECONDS
        ) {
            "Block duration is outside the allowed range."
        }

        require(detectionCount >= 0) {
            "Detection count cannot be negative."
        }
    }

    /**
     * Checks whether this rule applies to a browser.
     */
    fun appliesToBrowser(packageName: String): Boolean {

        if (!enabled) return false

        return when (scope) {

            RuleScope.ALL_APPS,
            RuleScope.ALL_BROWSERS -> true

            RuleScope.SELECTED_BROWSERS,
            RuleScope.SELECTED_APPS -> {
                targetBrowsers.isEmpty() ||
                    packageName in targetBrowsers
            }
        }
    }

    /**
     * Checks whether a hostname matches this rule.
     */
    fun matchesDomain(hostname: String): Boolean {

        if (!enabled) return false

        val normalizedHost =
            normalizeDomain(hostname)

        val normalizedRule =
            normalizeDomain(domain)

        if (normalizedHost.isEmpty() ||
            normalizedRule.isEmpty()
        ) {
            return false
        }

        if (normalizedHost == normalizedRule) {
            return true
        }

        return includeSubdomains &&
            normalizedHost.endsWith(".$normalizedRule")
    }

    private fun normalizeDomain(value: String): String {

        return value
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
