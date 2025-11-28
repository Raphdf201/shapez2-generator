package net.raphdf201.shapez2generator.fileBuilders

fun genGitignoreFile(): String {
    return """
bin/
obj/
logs/
/packages/
riderModule.iml
/_ReSharper.Caches/
"""
}
