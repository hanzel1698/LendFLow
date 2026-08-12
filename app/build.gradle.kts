import java.io.File
import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
}

data class UploadSigningConfig(
    val storeFile: File,
    val storePassword: String,
    val keyAlias: String,
    val keyPassword: String,
)

fun resolveUploadSigning(): UploadSigningConfig? {
    System.getenv("KEYSTORE_FILE")?.takeIf { it.isNotBlank() }?.let { path ->
        val file = File(path)
        if (file.isFile && file.length() > 100L) {
            return UploadSigningConfig(
                storeFile = file,
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "android",
                keyAlias = System.getenv("KEY_ALIAS") ?: "androiddebugkey",
                keyPassword = System.getenv("KEY_PASSWORD") ?: "android",
            )
        }
    }
    val centralDir = File(System.getProperty("user.home"), ".android/signing")
    val centralKeystore = File(centralDir, "upload-keystore.jks")
    if (centralKeystore.isFile && centralKeystore.length() > 100L) {
        val props = Properties()
        File(centralDir, "signing.properties").takeIf { it.isFile }?.inputStream()?.use {
            props.load(it)
        }
        return UploadSigningConfig(
            storeFile = centralKeystore,
            storePassword = props.getProperty("storePassword", "android"),
            keyAlias = props.getProperty("keyAlias", "androiddebugkey"),
            keyPassword = props.getProperty("keyPassword", "android"),
        )
    }
    return null
}

val uploadSigning = resolveUploadSigning()

// CI overrides the version code so every distributed build is a distinct release.
// Play builds leave CI_VERSION_CODE unset and use the committed value below, which
// play-version-bump-build.yml owns and increments.
val ciVersionCode = System.getenv("CI_VERSION_CODE")?.toIntOrNull()?.takeIf { it > 0 }

android {
    namespace = "com.example.loantracker"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.loantracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        ciVersionCode?.let { build ->
            versionCode = build
            // Built by concatenation, not a string literal, so the versionName sed in
            // play-version-bump-build.yml cannot rewrite this line.
            versionName = versionName!!.substringBefore('.') + "." + build
        }
    }

    signingConfigs {
        if (uploadSigning != null) {
            create("upload") {
                storeFile = uploadSigning.storeFile
                storePassword = uploadSigning.storePassword
                keyAlias = uploadSigning.keyAlias
                keyPassword = uploadSigning.keyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("upload")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
      compose = true
      aidl = false
      buildConfig = false
      shaders = false
    }

    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)

  // Core Android dependencies
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)

  // Arch Components
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  // Compose
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  // Tooling
  debugImplementation(libs.androidx.compose.ui.tooling)
  // Instrumented tests
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  // Local tests: jUnit, coroutines, Android runner
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)

  // Instrumented tests: jUnit rules and runners
  androidTestImplementation(libs.androidx.test.core)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.espresso.core)

  // Navigation
  implementation(libs.androidx.navigation3.ui)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)

  // Room
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)

  // Material Icons Extended
  implementation("androidx.compose.material:material-icons-extended")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
