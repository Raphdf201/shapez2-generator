package net.raphdf201.shapez2generator.fileBuilders

fun genMainFile(projectId: String, loggerIncluded: Boolean, shapezShifterIncluded: Boolean): String {
    return """${if (loggerIncluded) "using Core.Logging;" else ""}
${if (shapezShifterIncluded) "using ShapezShifter.Kit;" else ""}

namespace $projectId;

public class Main : IMod
{
    public Main(${if (loggerIncluded) "ILogger logger" else ""})
    {
        ModFolderLocator res = ModDirectoryLocator.CreateLocator<Main>().SubLocator("Resources");
    }

    public void Dispose()
    {
    }
}
"""
}
