import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlinVersion: String by extra
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.0")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
    }
}

configure(allprojects) {

    group = "ru.chsergeig.fb2reader"
    version = rootProject.property("currentVersion") ?: "local"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        jcenter()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
