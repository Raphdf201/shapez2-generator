package net.raphdf201.shapez2generator.fileBuilders

fun genSolutionXFile(projectId: String): String {
    return """
<Solution>
  <Project Path="$projectId.csproj" />
</Solution>
""".trimIndent()
}
