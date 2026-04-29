package net.raphdf201.shapez2generator

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.json.jsonb

lateinit var db: DbService

fun database() {
    db = DbService(
        Database.connect(
            url = "jdbc:postgresql://$dbUrl",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
    )
}

class DbService(db: Database) {
    object WorkshopItems : Table("workshop_items") {
        val id = uinteger("id").uniqueIndex()
        val lastSteamUpdate = long("laststeamupdate")
        val lastLocalUpdate = long("lastlocalupdate")
        val steamName = varchar("steamtitle", 128)
        val dlls = array("dlls", VarCharColumnType())
        val latestVersion = varchar("latestversion", 32)
        val tags = jsonb<List<Tag>>("tags", Json.Default)
        override val primaryKey = PrimaryKey(id)
    }

    suspend fun create(item: DbWorkshopItem) = suspendTransaction {
        WorkshopItems.insert {
            it[id] = item.id
            it[lastSteamUpdate] = item.lastSteamUpdate
            it[lastLocalUpdate] = item.lastLocalUpdate
            it[steamName] = item.steamName
            it[dlls] = item.dlls
            it[latestVersion] = item.latestVersion
            it[tags] = item.tags
        }
    }

    suspend fun createAndGet(item: DbWorkshopItem): DbWorkshopItem {
        create(item)
        return item
    }

    suspend fun read(id: UInt): DbWorkshopItem? = suspendTransaction {
        WorkshopItems.selectAll()
            .where { WorkshopItems.id eq id }
            .map {
                DbWorkshopItem(
                    it[WorkshopItems.id],
                    it[WorkshopItems.lastSteamUpdate],
                    it[WorkshopItems.lastLocalUpdate],
                    it[WorkshopItems.steamName],
                    it[WorkshopItems.dlls],
                    it[WorkshopItems.latestVersion],
                    it[WorkshopItems.tags]
                )
            }
            .singleOrNull()
    }

    suspend fun update(item: DbWorkshopItem) = suspendTransaction {
        WorkshopItems.update({ WorkshopItems.id eq item.id }) {
            it[lastSteamUpdate] = item.lastSteamUpdate
            it[lastLocalUpdate] = item.lastLocalUpdate
            it[steamName] = item.steamName
            it[dlls] = item.dlls
            it[latestVersion] = item.latestVersion
            it[tags] = item.tags
        }
    }

    private suspend fun delete(id: UInt) = suspendTransaction {
        WorkshopItems.deleteWhere { WorkshopItems.id eq id }
    }
}
