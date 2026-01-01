package net.raphdf201.shapez2generator.npm

external interface Blob

external fun saveAs(blob: Blob, filename: String)

@OptIn(ExperimentalWasmJsInterop::class)
fun createTextBlob(content: String): Blob = js("new Blob([content], { type: 'text/plain' })")
