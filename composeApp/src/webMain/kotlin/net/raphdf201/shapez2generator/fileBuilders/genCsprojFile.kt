package net.raphdf201.shapez2generator.fileBuilders

import net.raphdf201.shapez2generator.npm.createZip
import net.raphdf201.shapez2generator.npm.createZipOptions
import net.raphdf201.shapez2generator.npm.saveAs

fun genCsprojFile(projectId: String, langVersion: Int, assemblies: List<Assembly>, shapezShifter: Boolean): String {
    val publicizedItems = assemblies
        .filter { it.publicized && it.included }.joinToString("\n") {
            """
    <ItemGroup>
        <Publicize Include="${it.name}" />
    </ItemGroup>"""
        }

    val includedAssemblies = assemblies
        .filter { it.included }.joinToString("\n") {
            """
        <Reference Include="${it.name.dropLast(4)}">
            <HintPath>$(SPZ2_PATH)\${it.name}</HintPath>
            <Private>False</Private>
        </Reference>"""
        }

    val spzShifter = if (shapezShifter) """
        <Reference Include="ShapezShifter">
            <HintPath>$(SPZ2_SHIFTER)</HintPath>
            <Private>False</Private>
        </Reference>""" else ""
    return """<Project Sdk="Microsoft.NET.Sdk">
    <PropertyGroup>
        <TargetFramework>netstandard2.1</TargetFramework>
        <Nullable>disable</Nullable>
        <RunPostBuildEvent>Always</RunPostBuildEvent>
        <LangVersion>$langVersion</LangVersion>
        <RootNamespace>$projectId</RootNamespace>
    </PropertyGroup>

    <PropertyGroup>
        <OutputPath>$(SPZ2_PERSISTENT)\mods\$projectId</OutputPath>
        <AppendTargetFrameworkToOutputPath>false</AppendTargetFrameworkToOutputPath>
    </PropertyGroup>
    <PropertyGroup>
        <PublicizerLogFilePath>logs/krafs</PublicizerLogFilePath>
    </PropertyGroup>

    <Target Name="SteamPublish">
        <Exec Command='sh .\Steam\SteamPublish.sh "$(OutputPath)'/>
    </Target>

    <ItemGroup>
        <PackageReference Include="MonoMod.RuntimeDetour" Version="25.3.0"/>
        <PackageReference Include="Krafs.Publicizer" Version="2.3.0">
            <PrivateAssets>all</PrivateAssets>
            <IncludeAssets>runtime; build; native; contentfiles; analyzers; buildtransitive</IncludeAssets>
        </PackageReference>
    </ItemGroup>

    <ItemGroup>
        <None Update="manifest.json">
            <CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
        </None>
        <None Update="translations.json">
            <CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
        </None>
        <None Update="Resources/*">
            <CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
        </None>
    </ItemGroup>

    $publicizedItems

    <ItemGroup>
        $spzShifter
        $includedAssemblies
    </ItemGroup>
</Project>
"""
}

fun genAndDownloadCsproj(
    projectId: String,
    langVersion: Int,
    modDependencies: List<ManifestDependency>,
    assemblies: List<Assembly>
) {

    val zip = createZip()

    zip.file("$projectId.csproj", genCsprojFile(projectId, langVersion, assemblies, modDependencies[0].ModTitle == "ShapezShifter"))

    val blob = zip.generate(createZipOptions())

    saveAs(blob, "$projectId.zip")
}

data class Assembly(
    val name: String,
    val included: Boolean,
    val publicized: Boolean
)
