package net.raphdf201.shapez2generator

import kotlinx.serialization.json.Json

fun prettifyJson(jsonString: String): String {
    return try {
        prettyJson.encodeToString(kotlinx.serialization.json.JsonElement.serializer(), Json.parseToJsonElement(jsonString))
    } catch (e: Exception) {
        e.printStackTrace()
        jsonString
    }
}
