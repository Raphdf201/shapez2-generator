package net.raphdf201.shapez2generator.npm

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalWasmJsInterop::class)
fun createZip(): JSZip = js("new JSZip()")

// Helper function for generate options
@OptIn(ExperimentalWasmJsInterop::class)
fun createZipOptions(): JSZipOptions = js("({ type: 'blob', compression: 'DEFLATE' })")
external class JSZip {
    fun file(name: String, content: String)
    fun generate(options: JSZipOptions): Blob
}

external interface JSZipOptions {
    var type: String
    var compression: String
}
