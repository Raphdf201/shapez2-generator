package net.raphdf201.shapez2generator

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

lateinit var db: DbService

fun database() {
    db = DbService(Database.connect(
        url = "jdbc:postgresql://${dbUrl}",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    ))
}

class DbService(db: Database) {
    object WorkshopItems : Table("workshop_items") {
        val id = uinteger("id").uniqueIndex()
        val lastSteamUpdate = long("laststeamupdate")
        val lastLocalUpdate = long("lastlocalupdate")
        val manifName = varchar("maniftitle", 128)
        val steamName = varchar("steamtitle", 128)
        val dlls = array("dlls", VarCharColumnType())
        val latestVersion = varchar("latestversion", 32)
        // if schema changes, uncomment init
        override val primaryKey = PrimaryKey(id)
    }

    /*init {
        transaction(db) {
            SchemaUtils.drop(WorkshopItems)
            SchemaUtils.create(WorkshopItems)
        }
    }*/

    suspend fun create(item: DbWorkshopItem) = suspendTransaction {
        WorkshopItems.insert {
            it[id] = item.id
            it[lastSteamUpdate] = item.lastSteamUpdate
            it[lastLocalUpdate] = item.lastLocalUpdate
            it[manifName] = item.manifestName
            it[steamName] = item.steamName
            it[dlls] = item.dlls
            it[latestVersion] = item.latestVersion
        }
    }

    suspend fun createAndGet(item: DbWorkshopItem): DbWorkshopItem {
        create(item)
        return item
    }

    suspend fun read(id: UInt): DbWorkshopItem? = suspendTransaction {
        WorkshopItems.selectAll()
            .where { WorkshopItems.id eq id }
            .map { DbWorkshopItem(
                it[WorkshopItems.id],
                it[WorkshopItems.lastSteamUpdate],
                it[WorkshopItems.lastLocalUpdate],
                it[WorkshopItems.manifName],
                it[WorkshopItems.steamName],
                it[WorkshopItems.dlls],
                it[WorkshopItems.latestVersion],
            ) }
            .singleOrNull()
    }

    suspend fun update(item: DbWorkshopItem) = suspendTransaction {
        WorkshopItems.update({ WorkshopItems.id eq item.id }) {
            it[lastSteamUpdate] = item.lastSteamUpdate
            it[lastLocalUpdate] = item.lastLocalUpdate
            it[manifName] = item.manifestName
            it[steamName] = item.steamName
            it[dlls] = item.dlls
            it[latestVersion] = item.latestVersion
        }
    }

    private suspend fun delete(id: UInt) = suspendTransaction {
        WorkshopItems.deleteWhere { WorkshopItems.id eq id }
    }
}
