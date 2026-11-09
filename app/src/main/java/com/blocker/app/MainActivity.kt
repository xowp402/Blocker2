package com.blocker.app

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class LaunchableApp(val packageName: String, val label: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Blocker2Screen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Blocker2Screen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(AccessibilityMonitorService.PREFS, 0) }
    var input by remember { mutableStateOf("") }
    var words by remember {
        mutableStateOf(prefs.getStringSet(AccessibilityMonitorService.KEY_BLOCKED_WORDS, emptySet())
            ?.toList()?.sorted().orEmpty())
    }
    var duration by remember {
        mutableIntStateOf(prefs.getInt(AccessibilityMonitorService.KEY_BLOCK_MINUTES, 2).coerceIn(1, 120))
    }
    var monitorAll by remember {
        mutableStateOf(prefs.getBoolean(AccessibilityMonitorService.KEY_MONITOR_ALL_APPS, true))
    }
    var selectedPackages by remember {
        mutableStateOf(prefs.getStringSet(AccessibilityMonitorService.KEY_MONITORED_PACKAGES, emptySet()).orEmpty())
    }

    val launchableApps = remember(context) {
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        context.packageManager.queryIntentActivities(launcherIntent, PackageManager.MATCH_ALL)
            .mapNotNull { info ->
                val activity = info.activityInfo ?: return@mapNotNull null
                if (activity.packageName == context.packageName) return@mapNotNull null
                LaunchableApp(
                    packageName = activity.packageName,
                    label = info.loadLabel(context.packageManager)?.toString()
                        ?.takeIf { it.isNotBlank() } ?: activity.packageName
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }

    fun saveAppSelection(all: Boolean, selected: Set<String>) {
        prefs.edit()
            .putBoolean(AccessibilityMonitorService.KEY_MONITOR_ALL_APPS, all)
            .putStringSet(AccessibilityMonitorService.KEY_MONITORED_PACKAGES, selected)
            .apply()
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = androidx.compose.ui.graphics.Color(0xFF76A9FA),
        secondary = androidx.compose.ui.graphics.Color(0xFF8DD8C7),
        background = androidx.compose.ui.graphics.Color(0xFF10141D),
        surface = androidx.compose.ui.graphics.Color(0xFF1B2330)
    )) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Blocker2", fontWeight = FontWeight.Bold) }) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item {
                    Card(shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("داشبورد", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                            Text("فیلتر واژه‌ها به‌صورت محلی روی دستگاه اجرا می‌شود.")
                            Text("واژه‌های فعال: ${words.size}")
                            Text(if (monitorAll) "پایش: همه برنامه‌های دیگر" else "پایش: ${selectedPackages.size} برنامه انتخاب‌شده")
                            Button(
                                onClick = { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("بازکردن تنظیمات دسترس‌پذیری") }
                        }
                    }
                }

                item {
                    Card(shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("مدیریت واژه‌ها", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = input,
                                onValueChange = { input = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("واژه یا عبارت جدید") },
                                singleLine = true
                            )
                            Button(onClick = {
                                val value = input.trim()
                                if (value.isNotEmpty() && words.none { it.equals(value, true) }) {
                                    words = (words + value).sorted()
                                    prefs.edit().putStringSet(
                                        AccessibilityMonitorService.KEY_BLOCKED_WORDS, words.toSet()
                                    ).apply()
                                }
                                input = ""
                            }, modifier = Modifier.fillMaxWidth()) { Text("افزودن واژه") }

                            Text("مدت خروج از برنامه پس از تشخیص")
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(1, 2, 5, 10, 15, 30, 60).forEach { value ->
                                    FilterChip(
                                        selected = duration == value,
                                        onClick = {
                                            duration = value
                                            prefs.edit().putInt(
                                                AccessibilityMonitorService.KEY_BLOCK_MINUTES, value
                                            ).apply()
                                        },
                                        label = { Text("$value دقیقه") }
                                    )
                                }
                            }
                        }
                    }
                }

                item { Text("واژه‌های ثبت‌شده (${words.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                items(words, key = { it }) { word ->
                    Card {
                        Row(
                            Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(word, modifier = Modifier.weight(1f))
                            TextButton(onClick = {
                                words = words.filterNot { it == word }
                                prefs.edit().putStringSet(
                                    AccessibilityMonitorService.KEY_BLOCKED_WORDS, words.toSet()
                                ).apply()
                            }) { Text("حذف") }
                        }
                    }
                }

                item {
                    Card(shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("برنامه‌های تحت پایش", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = monitorAll,
                                    onCheckedChange = {
                                        monitorAll = it
                                        saveAppSelection(monitorAll, selectedPackages)
                                    }
                                )
                                Text("پایش همه برنامه‌ها به‌جز Blocker2")
                            }
                            Text(
                                if (monitorAll) "برای انتخاب دستی، گزینه بالا را خاموش کن."
                                else "فقط برنامه‌های انتخاب‌شده بررسی می‌شوند.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (!monitorAll) {
                    items(launchableApps, key = { it.packageName }) { app ->
                        Card {
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(app.label, fontWeight = FontWeight.Medium)
                                    Text(app.packageName, style = MaterialTheme.typography.labelSmall)
                                }
                                Checkbox(
                                    checked = app.packageName in selectedPackages,
                                    onCheckedChange = { checked ->
                                        selectedPackages = if (checked) {
                                            selectedPackages + app.packageName
                                        } else {
                                            selectedPackages - app.packageName
                                        }
                                        saveAppSelection(false, selectedPackages)
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        "محدودیت فنی: فقط متنی بررسی می‌شود که هر برنامه از طریق سرویس دسترس‌پذیری در اختیار اندروید قرار دهد. همه پیام‌ها و صفحات قابل‌خواندن نیستند. هیچ متن یا سابقه‌ای به سرور ارسال نمی‌شود.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}