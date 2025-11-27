package net.raphdf201.shapez2generator.fileBuilders

fun genMainFile(projectId: String): String {
    return """
using Core.Logging;
using ShapezShifter.Kit;

namespace $projectId;

public class Main : IMod
{
    public Main(ILogger logger)
    {
        ModFolderLocator res = ModDirectoryLocator.CreateLocator<Main>().SubLocator("Resources");
    }

    public void Dispose()
    {
    }
}
"""
}
