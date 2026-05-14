# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# ---- Retrofit ----
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# ---- OkHttp ----
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# ---- Kotlin Serialization ----
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.namma.platform.**$$serializer { *; }
-keepclassmembers class com.namma.platform.** {
    *** Companion;
}
-keepclasseswithmembers class com.namma.platform.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep class com.namma.platform.data.local.entity.** { *; }

# ---- Firebase ----
-keep class com.google.firebase.** { *; }

# ---- Google Maps ----
-keep class com.google.android.gms.maps.** { *; }
-keep class com.google.maps.android.** { *; }

# ---- ERail DTOs ----
-keep class com.namma.platform.data.remote.dto.** { *; }
