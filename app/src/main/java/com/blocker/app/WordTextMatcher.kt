package com.blocker.app

import java.util.Locale

/**
 * Persian/Arabic-aware whole-word matcher.
 *
 * ZWNJ/ZWJ are removed (so Persian half-space variants match), ordinary
 * whitespace is collapsed, and word boundaries are retained to avoid
 * matching a short rule inside a longer word.
 */
object WordTextMatcher {
    fun containsRule(text: String, rule: String): Boolean {
        val normalizedText = normalize(text)
        val normalizedRule = normalize(rule)
        if (normalizedRule.isEmpty()) return false
        val pattern = Regex(
            "(?<![\\\\p{L}\\\\p{N}])${Regex.escape(normalizedRule)}(?![\\\\p{L}\\\\p{N}])"
        )
        return pattern.containsMatchIn(normalizedText)
    }

    fun normalize(value: String): String = value
        .lowercase(Locale.ROOT)
        .replace('ي', 'ی')
        .replace('ى', 'ی')
        .replace('ك', 'ک')
        .replace(Regex("[\\\\u064B-\\\\u065F\\\\u0670\\\\u0640]"), "")
        .replace(Regex("[\\\\u200B\\\\u200C\\\\u200D\\\\uFEFF]"), "")
        .replace(Regex("\\\\s+"), " ")
        .trim()
}