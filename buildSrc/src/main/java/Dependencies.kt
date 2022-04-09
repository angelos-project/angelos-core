// https://github.com/pahlevikun/KotlinGradle

object AngelosVersion {
    const val angelos = "0.0.1"
    const val tradeMark = "Angelos™"
    const val logoTradeMark = "Logo Messenger™"
}

object AndroidVersion {
    const val compileSdk = 30
    const val minSdk = 23
    const val targetSdk = 30
    const val versionCode = 1
    const val buildToolsVersion = "30.0.2"
    const val versionName = "1.0.0"
}

object Versions {
    const val gradle = "7.3.3"
    const val android = "7.0.2"
    const val kotlin = "1.6.10"
    const val appcompat = "1.1.0"
    const val coro = "1.6.0"
    const val stately = "1.2.0"

    const val junit = "4.13.1"
}

object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

    const val coro = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coro}-native-mt"
    const val stately = "co.touchlab:stately-common:${Versions.stately}"
    const val concurrency = "co.touchlab:stately-concurrency:${Versions.stately}"
    const val collections = "co.touchlab:stately-iso-collections:${Versions.stately}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val coro = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coro}"
}