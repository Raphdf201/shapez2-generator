package net.raphdf201.shapez2generator

import kotlinx.serialization.json.Json

fun prettifyJson(jsonString: String): String {
    val jsonElement = Json.parseToJsonElement(jsonString)
    return prettyJson.encodeToString(kotlinx.serialization.json.JsonElement.serializer(), jsonElement)
}
