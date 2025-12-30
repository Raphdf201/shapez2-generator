package net.raphdf201.shapez2generator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

const val SERVER_PORT = 7000

@OptIn(ExperimentalSerializationApi::class)
val prettyJson = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

val notStrictJson = Json {
    ignoreUnknownKeys = true
}
