# Preserve runtime annotations and generic type information.
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Kotlin metadata
-keep class kotlin.Metadata { *; }

# Kotlin Serialization
-keepclassmembers class **$$serializer {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class ** {
    *** Companion;
}

# Preserve Android components referenced by the manifest.
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.accessibilityservice.AccessibilityService
-keep public class * extends android.content.BroadcastReceiver
