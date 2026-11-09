package com.blocker.app

import android.accessibilityservice.AccessibilityService
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/** Local-only scanner. Blocker2 itself is explicitly excluded. */
class AccessibilityMonitorService : AccessibilityService() {
    private val blockedUntil = mutableMapOf<String, Long>()
    private var lastTriggerAt = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        val targetPackage = event.packageName?.toString() ?: return
        if (targetPackage.isBlank() || targetPackage == packageName) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) return

        val now = SystemClock.elapsedRealtime()
        if ((blockedUntil[targetPackage] ?: 0L) > now) {
            performGlobalAction(GLOBAL_ACTION_HOME)
            return
        }

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val words = prefs.getStringSet("blocked_words", emptySet()).orEmpty()
        if (words.isEmpty()) return

        val text = buildString {
            event.text.forEach { append(it).append(' ') }
            appendNodeText(rootInActiveWindow, this, 0)
        }
        if (text.isBlank()) return
        val normalizedText = normalize(text)
        if (words.none { it.isNotBlank() && normalize(it) in normalizedText }) return
        if (now - lastTriggerAt < 900L) return

        lastTriggerAt = now
        val minutes = prefs.getInt("block_minutes", 2).coerceIn(1, 120)
        blockedUntil[targetPackage] = now + minutes * 60_000L
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun appendNodeText(node: AccessibilityNodeInfo?, out: StringBuilder, depth: Int) {
        if (node == null || depth > 18 || out.length > 16_000) return
        node.text?.let { out.append(it).append(' ') }
        node.contentDescription?.let { out.append(it).append(' ') }
        for (i in 0 until node.childCount) appendNodeText(node.getChild(i), out, depth + 1)
    }

    private fun normalize(value: String): String = value.lowercase()
        .replace('ي', 'ی').replace('ك', 'ک')
        .replace(Regex("[\\u064B-\\u065F\\u0670]"), "")
        .replace(Regex("[\\s\\u200c\\u200d]+"), "")

    override fun onInterrupt() = Unit

    companion object { const val PREFS = "blocker2_local_settings" }
}