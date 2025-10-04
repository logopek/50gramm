import java.io.FileInputStream
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
}
val localProps = Properties()
val localFile = rootProject.file("local.properties")
localProps.load(FileInputStream(localFile))
android {
	namespace = "ni.shikatu.a50gramm"
	compileSdk = 36

	defaultConfig {
		applicationId = "ni.shikatu.a50gramm"
		minSdk = 26
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"
		buildFeatures {
			buildConfig = true
		}
		buildConfigField("int", "API_ID", localProps.getProperty("API_ID"))
		buildConfigField("String", "API_HASH", "\"${localProps.getProperty("API_HASH")}\"")

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
}

dependencies {
	implementation(project(":tdlib"))
	implementation(libs.androidx.lifecycle.viewmodel.compose)
	implementation(libs.coil.compose)
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.material3)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.tooling)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
}