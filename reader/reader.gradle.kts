plugins {
    java
    application
}

application {
    mainClassName = "ru.chsergeig.fb2reader.Xmain"
}

dependencies {
    implementation("org.jodd:jodd-core:5.1.3")
    implementation("org.jodd:jodd-lagarto:5.1.3")


//    implementation("org.jsoup:jsoup:1.12.2")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}
