plugins { id("com.android.application");
 id("org.jetbrains.kotlin.android");
 id("com.google.gms.google-services") }
android { namespace="com.example.eventmanagement"
 compileSdk=35
 defaultConfig {
  applicationId="com.example.eventmanagement"
  minSdk=23
  targetSdk=35
  versionCode=1;
  versionName="1.0"
 }
}
kotlin {
 jvmToolchain(17)
}
dependencies {
 implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
 implementation("com.google.firebase:firebase-auth-ktx")
 implementation("com.google.firebase:firebase-firestore-ktx")
 implementation("com.google.firebase:firebase-messaging-ktx")
 implementation("androidx.core:core-ktx:1.15.0")
 implementation("androidx.appcompat:appcompat:1.7.0")
 implementation("com.google.android.material:material:1.12.0")
 implementation("androidx.activity:activity-ktx:1.10.0");
 implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
 implementation("androidx.recyclerview:recyclerview:1.3.2")
 implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
 implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
 testImplementation("junit:junit:4.13.2")
 testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
 testImplementation("io.mockk:mockk:1.13.13")
}
