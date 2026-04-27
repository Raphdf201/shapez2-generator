package net.raphdf201.shapez2generator

fun DbWorkshopItem.toSharedWorkshopItem(): SharedWorkshopItem = SharedWorkshopItem(
    this.id,
    this.steamName,
    this.dlls,
    this.latestVersion
)

fun String.removeWhitespace() = this.replace(" ", "")
