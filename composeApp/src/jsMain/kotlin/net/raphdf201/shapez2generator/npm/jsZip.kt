@file:JsModule("jszip")
@file:JsNonModule

package net.raphdf201.shapez2generator.npm

external class JSZip {
    fun file(name: String, content: String)
    fun generateAsync(options: dynamic): dynamic
}
