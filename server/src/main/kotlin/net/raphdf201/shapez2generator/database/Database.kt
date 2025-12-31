package net.raphdf201.shapez2generator.database

import net.raphdf201.shapez2generator.WorkshopItem
import net.raphdf201.shapez2generator.config
import org.jetbrains.exposed.v1.core.Table
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
        url = "jdbc:postgresql://${config[1]}",
        driver = "org.postgresql.Driver",
        user = config[2],
        password = config[3]
    ))
}

class DbService(db: Database) {
    object WorkshopItems : Table("workshop_items") {
        val id = uinteger("id").uniqueIndex()
        val title = varchar("title", 128)
        val dllName = varchar("dllname", 128)
        val latestVersion = varchar("latestversion", 32)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(db) {
            SchemaUtils.drop(WorkshopItems)// TODO : NEVER LET THIS IN PRODUCTION
            SchemaUtils.create(WorkshopItems)
        }
    }

    suspend fun create(item: WorkshopItem) = suspendTransaction {
        WorkshopItems.insert {
            it[title] = item.title
            it[dllName] = item.dllName
            it[latestVersion] = item.latestVersion
        }[WorkshopItems.id]
    }

    suspend fun createAndGet(item: WorkshopItem): WorkshopItem {
        suspendTransaction {
            WorkshopItems.insert {
                it[title] = item.title
                it[dllName] = item.dllName
                it[latestVersion] = item.latestVersion
            }[WorkshopItems.id]
        }
        return item
    }

    suspend fun read(id: UInt): WorkshopItem? = suspendTransaction {
        WorkshopItems.selectAll()
            .where { WorkshopItems.id eq id }
            .map { WorkshopItem(
                it[WorkshopItems.id],
                it[WorkshopItems.title],
                it[WorkshopItems.dllName],
                it[WorkshopItems.latestVersion]
            ) }
            .singleOrNull()
    }

    suspend fun update(item: WorkshopItem) = suspendTransaction {
        WorkshopItems.update({ WorkshopItems.id eq item.id }) {
            it[title] = item.title
            it[dllName] = item.dllName
            it[latestVersion] = item.latestVersion
        }
    }

    private suspend fun delete(id: UInt) = suspendTransaction {
        WorkshopItems.deleteWhere { WorkshopItems.id eq id }
    }
}
