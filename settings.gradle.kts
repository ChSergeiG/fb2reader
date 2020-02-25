rootProject.buildFileName = "build.gradle.kts"
rootProject.name = "fb2reader"

include(":reader", ":otherfb2reader")

rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle"
    if (!project.buildFile.isFile) {
        project.buildFileName = "${project.name}.gradle.kts"
    }
    if (!project.buildFile.isFile) {
        project.buildFileName = "build.gradle"
    }
    require(project.buildFile.isFile) {
        "${project.buildFile} must exist"
    }
}
