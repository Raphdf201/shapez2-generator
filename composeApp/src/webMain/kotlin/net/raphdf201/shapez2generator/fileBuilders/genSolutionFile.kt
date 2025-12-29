package net.raphdf201.shapez2generator.fileBuilders

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun genSolutionFile(projectId: String, useSlnX: Boolean): String = if (useSlnX) {
    return """
<Solution>
  <Project Path="$projectId.csproj" />
</Solution>
""".trimIndent()
} else {
    val projectGuid = Uuid.random().toString().uppercase()
    return """
Microsoft Visual Studio Solution File, Format Version 12.00
Project("{FAE04EC0-301F-11D3-BF4B-00C04F79EFBC}") = "$projectId", "$projectId.csproj", "{$projectGuid}"
EndProject
Global
	GlobalSection(SolutionConfigurationPlatforms) = preSolution
		Debug|Any CPU = Debug|Any CPU
		Release|Any CPU = Release|Any CPU
	EndGlobalSection
	GlobalSection(ProjectConfigurationPlatforms) = postSolution
		{$projectGuid}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{$projectGuid}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{$projectGuid}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{$projectGuid}.Release|Any CPU.Build.0 = Release|Any CPU
	EndGlobalSection
EndGlobal
"""
}
