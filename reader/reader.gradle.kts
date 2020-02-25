plugins {
    java
    application
}

application {
    mainClassName = "ru.chsergeig.fb2reader.Xmain"
}

dependencies {
    implementation("org.jsoup:jsoup:1.12.2")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}
