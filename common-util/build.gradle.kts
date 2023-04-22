plugins {
    id("mykim.android.library")
    id("mykim.android.hilt")
}

android {
    namespace = "com.mykim.common_util"
}

dependencies {

    implementation(libs.bundles.android)
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.junit)
    implementation(libs.bundles.glide)
    implementation(libs.bundles.security)

    implementation(project(":core-model"))
}