package net.raphdf201.shapez2generator

fun SharedWorkshopItem.processExceptions(): SharedWorkshopItem {
    return when (this.id) {
        ModIds.MonoModRuntimeDetour.toUInt() -> this.copy(dlls = listOf())// MonoMod.RuntimeDetour
        else -> this
    }
}

class ModIds {
    companion object {
        const val MonoModRuntimeDetour = "3542712030"
        const val ShapezShifter = "3542611357"
    }
}
