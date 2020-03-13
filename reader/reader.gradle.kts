plugins {
    java
    application
}

application {
    mainClassName = "ru.chsergeig.fb2reader.Xmain"
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.3")
    implementation("org.jodd:jodd-core:5.1.3")
    implementation("org.jodd:jodd-lagarto:5.1.3")
    implementation("commons-io:commons-io:2.6")
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
}
