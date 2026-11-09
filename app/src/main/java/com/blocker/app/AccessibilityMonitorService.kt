package com.blocker.app

import android.accessibilityservice.AccessibilityService
import android.content.SharedPreferences
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.Locale

/**
 * Local-only word scanner.
 *
 * Android only exposes text made available by each target app through
 * AccessibilityNodeInfo. This service cannot guarantee access to every screen
 * or encrypted/inaccessible message. Blocker2 never scans its own UI.
 */
class AccessibilityMonitorService : AccessibilityService() {
    private var lastActionElapsed = 0L
    private val actionDebounceMs = 900L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        val targetPackage = event.packageName?.toString()?.takeIf { it.isNotBlank() } ?: return
        if (targetPackage == packageName || !isRelevantEvent(event.eventType)) return

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        if (!isPackageMonitored(prefs, targetPackage)) return

        val nowWall = System.currentTimeMillis()
        val blockedUntil = prefs.getLong(BLOCKED_UNTIL_PREFIX + targetPackage, 0L)
        if (blockedUntil > nowWall) {
            sendHomeIfNeeded(targetPackage)
            return
        }
        if (blockedUntil != 0L) {
            prefs.edit().remove(BLOCKED_UNTIL_PREFIX + targetPackage).apply()
        }

        val words = prefs.getStringSet(KEY_BLOCKED_WORDS, emptySet())
            .orEmpty()
            .filter(String::isNotBlank)
        if (words.isEmpty()) return

        val visibleText = buildString {
            event.text.forEach { append(it).append(' ') }
            appendNodeText(rootInActiveWindow, this, 0)
        }
        if (visibleText.isBlank()) return

        val matched = words.any { rule -> containsWholeRule(visibleText, rule) }
        if (!matched) return

        val durationMinutes = prefs.getInt(KEY_BLOCK_MINUTES, DEFAULT_BLOCK_MINUTES)
            .coerceIn(1, MAX_BLOCK_MINUTES)
        prefs.edit()
            .putLong(BLOCKED_UNTIL_PREFIX + targetPackage, nowWall + durationMinutes * 60_000L)
            .apply()
        sendHomeIfNeeded(targetPackage)
    }

    private fun isRelevantEvent(type: Int): Boolean =
        type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
            type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
            type == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED ||
            type == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED

    /**
     * Empty monitored_packages means monitor all third-party apps.
     * When the user selects packages, only those packages are monitored.
     */
    private fun isPackageMonitored(prefs: SharedPreferences, targetPackage: String): Boolean {
        val selected = prefs.getStringSet(KEY_MONITORED_PACKAGES, emptySet()).orEmpty()
        return prefs.getBoolean(KEY_MONITOR_ALL_APPS, true) || targetPackage in selected
    }

    private fun sendHomeIfNeeded(targetPackage: String) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastActionElapsed < actionDebounceMs) return
        lastActionElapsed = now
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun appendNodeText(node: AccessibilityNodeInfo?, out: StringBuilder, depth: Int) {
        if (node == null || depth > MAX_TREE_DEPTH || out.length >= MAX_SCANNED_CHARS) return
        node.text?.let { out.append(it).append(' ') }
        node.contentDescription?.let { out.append(it).append(' ') }
        for (index in 0 until node.childCount) {
            if (out.length >= MAX_SCANNED_CHARS) break
            appendNodeText(node.getChild(index), out, depth + 1)
        }
    }

    /**
     * Normalize Persian/Arabic variants and whitespace, but retain word
     * boundaries so a rule such as "سلام" does not match "سلامت".
     */
    private fun containsWholeRule(text: String, rule: String): Boolean {
        val normalizedText = normalize(text)
        val normalizedRule = normalize(rule)
        if (normalizedRule.isEmpty()) return false
        val escaped = Regex.escape(normalizedRule)
        val pattern = Regex("(?<![\\p{L}\\p{N}])$escaped(?![\\p{L}\\p{N}])")
        return pattern.containsMatchIn(normalizedText)
    }

    private fun normalize(value: String): String = value
        .lowercase(Locale.ROOT)
        .replace('ي', 'ی')
        .replace('ى', 'ی')
        .replace('ك', 'ک')
        .replace(Regex("[\\u064B-\\u065F\\u0670\\u0640]"), "")
        .replace(Regex("[\\s\\u200B\\u200C\\u200D\\uFEFF]+"), " ")
        .trim()

    override fun onInterrupt() = Unit

    companion object {
        const val PREFS = "blocker2_local_settings"
        const val KEY_BLOCKED_WORDS = "blocked_words"
        const val KEY_BLOCK_MINUTES = "block_minutes"
        const val KEY_MONITORED_PACKAGES = "monitored_packages"
        const val KEY_MONITOR_ALL_APPS = "monitor_all_apps"
        private const val BLOCKED_UNTIL_PREFIX = "blocked_until:"
        private const val DEFAULT_BLOCK_MINUTES = 2
        private const val MAX_BLOCK_MINUTES = 120
        private const val MAX_TREE_DEPTH = 18
        private const val MAX_SCANNED_CHARS = 16_000
    }
}