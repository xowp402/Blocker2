package com.blocker.core.common

/**
 * Resolves conflicts between matching Blocker rules.
 *
 * Active blocks have the highest priority.
 * When multiple rules match, the configured conflict
 * resolution strategy determines the winning rule.
 */
object RulePriorityResolver {

    /**
     * Selects the winning word rule.
     */
    fun resolveWordRules(
        rules: List<WordFilterConfig>,
        strategy: ConflictResolution
    ): WordFilterConfig? {

        val enabledRules = rules.filter { it.enabled }

        if (enabledRules.isEmpty()) {
            return null
        }

        return when (strategy) {

            ConflictResolution.MOST_RESTRICTIVE -> {
                enabledRules.maxWithOrNull(
                    compareBy<WordFilterConfig> {
                        it.blockDurationSeconds
                    }.thenBy {
                        it.priority
                    }
                )
            }

            ConflictResolution.MANUAL_PRIORITY -> {
                enabledRules.maxWithOrNull(
                    compareBy<WordFilterConfig> {
                        it.priority
                    }.thenBy {
                        it.blockDurationSeconds
                    }
                )
            }

            ConflictResolution.WARN_ONLY -> {
                enabledRules.maxByOrNull {
                    it.priority
                }
            }
        }
    }

    /**
     * Selects the winning website rule.
     */
    fun resolveWebsiteRules(
        rules: List<WebsiteRuleConfig>,
        strategy: ConflictResolution
    ): WebsiteRuleConfig? {

        val enabledRules = rules.filter { it.enabled }

        if (enabledRules.isEmpty()) {
            return null
        }

        return when (strategy) {

            ConflictResolution.MOST_RESTRICTIVE -> {
                enabledRules.maxWithOrNull(
                    compareBy<WebsiteRuleConfig> {
                        if (it.permanentBlock) {
                            Long.MAX_VALUE
                        } else {
                            it.blockDurationSeconds
                        }
                    }.thenBy {
                        it.priority
                    }
                )
            }

            ConflictResolution.MANUAL_PRIORITY -> {
                enabledRules.maxWithOrNull(
                    compareBy<WebsiteRuleConfig> {
                        it.priority
                    }.thenBy {
                        if (it.permanentBlock) {
                            Long.MAX_VALUE
                        } else {
                            it.blockDurationSeconds
                        }
                    }
                )
            }

            ConflictResolution.WARN_ONLY -> {
                enabledRules.maxByOrNull {
                    it.priority
                }
            }
        }
    }

    /**
     * Determines whether an existing active block
     * should remain in effect.
     */
    fun shouldKeepExistingBlock(
        existingBlock: ActiveBlock?,
        newBlock: ActiveBlock?
    ): Boolean {

        if (existingBlock == null) return false

        if (!existingBlock.isActive()) return false

        if (newBlock == null) return true

        if (newBlock.permanent && !existingBlock.permanent) {
            return false
        }

        if (existingBlock.permanent && !newBlock.permanent) {
            return true
        }

        return existingBlock.remainingSeconds() >=
            newBlock.remainingSeconds()
    }
}
