import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("mykim.android.library")
    id("mykim.android.hilt")
}

android {
    namespace = "com.mykim.core_database"
}

dependencies {

    implementation(libs.bundles.android)
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.junit)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.bundles.coroutine)
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)

    implementation(project(":common-util"))
    implementation(project(":core-model"))

}