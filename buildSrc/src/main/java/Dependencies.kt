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

    const val junit = "4.12"
}

object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
}