rootProject.name = "emmylib"

listOf(
    "common"
).forEach {
    include(it)
    findProject(":$it")?.name = "emmylib-$it"
}
