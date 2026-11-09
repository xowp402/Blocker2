package com.blocker.app

import android.content.Intent
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
    var words by remember { mutableStateOf(prefs.getStringSet("blocked_words", emptySet())?.toList()?.sorted().orEmpty()) }
    var duration by remember { mutableIntStateOf(prefs.getInt("block_minutes", 2)) }

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
                            Button(onClick = { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }, modifier = Modifier.fillMaxWidth()) {
                                Text("فعال‌سازی سرویس دسترس‌پذیری")
                            }
                        }
                    }
                }
                item {
                    Card(shape = RoundedCornerShape(22.dp)) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("مدیریت واژه‌ها", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(value = input, onValueChange = { input = it }, modifier = Modifier.fillMaxWidth(), label = { Text("واژه یا عبارت جدید") }, singleLine = true)
                            Button(onClick = {
                                val value = input.trim()
                                if (value.isNotEmpty() && words.none { it.equals(value, true) }) {
                                    words = (words + value).sorted()
                                    prefs.edit().putStringSet("blocked_words", words.toSet()).apply()
                                }
                                input = ""
                            }, modifier = Modifier.fillMaxWidth()) { Text("افزودن واژه") }
                            Text("مدت خروج از برنامه پس از تشخیص")
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(1, 2, 5, 10, 15).forEach { value ->
                                    FilterChip(selected = duration == value, onClick = {
                                        duration = value
                                        prefs.edit().putInt("block_minutes", value).apply()
                                    }, label = { Text("$value دقیقه") })
                                }
                            }
                        }
                    }
                }
                item { Text("واژه‌های ثبت‌شده (${words.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                items(words, key = { it }) { word ->
                    Card {
                        Row(Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(word, modifier = Modifier.weight(1f))
                            TextButton(onClick = {
                                words = words.filterNot { it == word }
                                prefs.edit().putStringSet("blocked_words", words.toSet()).apply()
                            }) { Text("حذف") }
                        }
                    }
                }
                item {
                    Text("Blocker2 از پایش خودش مستثناست. تشخیص متن فقط به محتوایی محدود است که اندروید از طریق سرویس دسترس‌پذیری ارائه می‌کند.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                }
            }
        }
    }
}