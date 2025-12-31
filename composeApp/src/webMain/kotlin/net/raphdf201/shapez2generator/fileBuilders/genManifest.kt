package net.raphdf201.shapez2generator.fileBuilders

import net.raphdf201.shapez2generator.Manifest
import net.raphdf201.shapez2generator.prettyJson

fun genManifestFile(config: Manifest): String {
    return prettyJson.encodeToString(config)
}
