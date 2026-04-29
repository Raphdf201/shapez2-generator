package net.raphdf201.shapez2generator.steam

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.raphdf201.shapez2generator.Tag
import net.raphdf201.shapez2generator.apikey
import net.raphdf201.shapez2generator.client
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

// https://partner.steamgames.com/doc/webapi/IPublishedFileService#QueryFiles

class IPublishedFileService {
    companion object {
        private const val URL = "https://api.steampowered.com/IPublishedFileService/QueryFiles/v1/"

        private var cache: List<PublishedFile>? = null
        private var cacheAge = Clock.System.now()
        private val cacheLock = Mutex()
        private var isFetching = false

        suspend fun getCache(): List<PublishedFile> {
            val cachedResult: List<PublishedFile>? = cacheLock.withLock {
                if (cache != null && Clock.System.now() - cacheAge < 60.seconds) {
                    return@withLock cache
                }
                if (isFetching) {
                    return@withLock cache
                }
                isFetching = true
                return@withLock null
            }
            if (cachedResult != null) return cachedResult

            val result = Json.decodeFromString<SteamWorkshopResponse>(client.get(URL) {
                url {
                    parameters.append("key", apikey)
                    parameters.append("input_json", Json.encodeToString(query))
                }
            }.bodyAsText()).response

            return cacheLock.withLock {
                isFetching = false
                cacheAge = Clock.System.now()
                cache = result.publishedFileDetails
                cache!!
            }
        }

        suspend fun getTags(): Set<Tag> {
            val tags = mutableSetOf<Tag>()
            getCache().forEach { tags.addAll(it.tags.orEmpty()) }
            return tags
        }

        private val query = WorkshopItemQuery(
            queryType = EPublishedFileQueryType.RANKED_BY_TOTAL_UNIQUE_SUBSCRIPTIONS.value,
            page = 1,
            cursor = "*",
            numPerPage = 1000,
            creatorAppId = 2162800,
            appId = 2162800,
            requiredTags = "",
            excludedTags = null,
            matchAllTags = false,
            requiredFlags = "",
            omittedFlags = "",
            searchText = "",
            fileType = EPublishedFileInfoMatchingFileType.ITEMS.value,
            childPublishedFileId = null,
            days = null,
            includeRecentVotesOnly = false,
            cacheMaxAgeSeconds = null,
            language = null,
            requiredKvTags = null,
            totalOnly = null,
            idsOnly = false,
            returnVoteData = false,
            returnTags = true,
            returnKvTags = false,
            returnPreviews = false,
            returnChildren = false,
            returnShortDescription = false,
            returnForSaleData = false,
            returnMetadata = true,
            returnPlaytimeStats = 0
        )

        @Serializable
        data class WorkshopItemQuery(
            @SerialName("query_type")
            val queryType: Int,

            @SerialName("page")
            val page: Int,

            @SerialName("cursor")
            val cursor: String,

            @SerialName("numperpage")
            val numPerPage: Int? = null,

            @SerialName("creator_appid")
            val creatorAppId: Int,

            @SerialName("appid")
            val appId: Int,

            @SerialName("requiredtags")
            val requiredTags: String,

            @SerialName("excludedtags")
            val excludedTags: String? = null,

            @SerialName("match_all_tags")
            val matchAllTags: Boolean,

            @SerialName("required_flags")
            val requiredFlags: String,

            @SerialName("omitted_flags")
            val omittedFlags: String,

            @SerialName("search_text")
            val searchText: String,

            @SerialName("filetype")
            val fileType: Int,

            @SerialName("child_publishedfileid")
            val childPublishedFileId: Long? = null,

            @SerialName("days")
            val days: Int? = null,

            @SerialName("include_recent_votes_only")
            val includeRecentVotesOnly: Boolean,

            @SerialName("cache_max_age_seconds")
            val cacheMaxAgeSeconds: Int? = null,

            @SerialName("language")
            val language: Int? = null,

            @SerialName("required_kv_tags")
            val requiredKvTags: Map<String, String>? = null,

            @SerialName("totalonly")
            val totalOnly: Boolean? = null,

            @SerialName("ids_only")
            val idsOnly: Boolean? = null,

            @SerialName("return_vote_data")
            val returnVoteData: Boolean,

            @SerialName("return_tags")
            val returnTags: Boolean,

            @SerialName("return_kv_tags")
            val returnKvTags: Boolean,

            @SerialName("return_previews")
            val returnPreviews: Boolean,

            @SerialName("return_children")
            val returnChildren: Boolean,

            @SerialName("return_short_description")
            val returnShortDescription: Boolean,

            @SerialName("return_for_sale_data")
            val returnForSaleData: Boolean,

            @SerialName("return_metadata")
            val returnMetadata: Boolean? = null,

            @SerialName("return_playtime_stats")
            val returnPlaytimeStats: Int
        )

        enum class EPublishedFileQueryType(val value: Int) {
            RANKED_BY_VOTE(0),
            RANKED_BY_PUBLICATION_DATE(1),
            ACCEPTED_FOR_GAME_RANKED_BY_ACCEPTANCE_DATE(2),
            RANKED_BY_TREND(3),
            FAVORITED_BY_FRIENDS_RANKED_BY_PUBLICATION_DATE(4),
            CREATED_BY_FRIENDS_RANKED_BY_PUBLICATION_DATE(5),
            RANKED_BY_NUM_TIMES_REPORTED(6),
            CREATED_BY_FOLLOWED_USERS_RANKED_BY_PUBLICATION_DATE(7),
            NOT_YET_RATED(8),
            RANKED_BY_TOTAL_UNIQUE_SUBSCRIPTIONS(9),
            RANKED_BY_TOTAL_VOTES_ASC(10),
            RANKED_BY_VOTES_UP(11),
            RANKED_BY_TEXT_SEARCH(12),
            RANKED_BY_PLAYTIME_TREND(13),
            RANKED_BY_TOTAL_PLAYTIME(14),
            RANKED_BY_AVERAGE_PLAYTIME_TREND(15),
            RANKED_BY_LIFETIME_AVERAGE_PLAYTIME(16),
            RANKED_BY_PLAYTIME_SESSIONS_TREND(17),
            RANKED_BY_LIFETIME_PLAYTIME_SESSIONS(18),
            RANKED_BY_INAPPROPRIATE_CONTENT_RATING(19),
            RANKED_BY_BAN_CONTENT_CHECK(20),
            RANKED_BY_LAST_UPDATED_DATE(21)
        }

        enum class EPublishedFileInfoMatchingFileType(val value: Int) {
            ITEMS(0),
            COLLECTIONS(1),
            ART(2),
            VIDEOS(3),
            SCREENSHOTS(4),
            COLLECTION_ELIGIBLE(5),
            GAMES(6),
            SOFTWARE(7),
            CONCEPTS(8),
            GREENLIGHT_ITEMS(9),
            ALL_GUIDES(10),
            WEB_GUIDES(11),
            INTEGRATED_GUIDES(12),
            USABLE_IN_GAME(13),
            MERCH(14),
            CONTROLLER_BINDINGS(15),
            STEAMWORKS_ACCESS_INVITES(16),
            ITEMS_MTX(17),
            ITEMS_READY_TO_USE(18),
            WORKSHOP_SHOWCASE(19),
            GAME_MANAGED_ITEMS(20)
        }
    }
}

@Serializable
data class SteamWorkshopResponse(
    val response: WorkshopResponse
)

@Serializable
data class PagedSteamWorkshopResponse(
    val response: PagedWorkshopResponse
)

@Serializable
data class WorkshopResponse(
    val total: Int,
    @SerialName("publishedfiledetails")
    val publishedFileDetails: List<PublishedFile>,
    @SerialName("next_cursor")
    val nextCursor: String? = null
)

@Serializable
data class PagedWorkshopResponse(
    val total: Int,
    @SerialName("publishedfiledetails")
    val publishedFileDetails: List<PublishedFile>? = null,
    @SerialName("next_cursor")
    val nextCursor: String? = null
)

@Serializable
data class PublishedFile(
    val result: Int,
    @SerialName("publishedfileid")
    val publishedFileId: String,
    val creator: String,
    @SerialName("creator_appid")
    val creatorAppId: Int,
    @SerialName("consumer_appid")
    val consumerAppId: Int,
    @SerialName("consumer_shortcutid")
    val consumerShortcutId: Int = 0,
    val filename: String = "",
    @SerialName("file_size")
    val fileSize: String,
    @SerialName("preview_file_size")
    val previewFileSize: String,
    @SerialName("preview_url")
    val previewUrl: String,
    val url: String = "",
    @SerialName("hcontent_file")
    val hcontentFile: String,
    @SerialName("hcontent_preview")
    val hcontentPreview: String,
    val title: String,
    @SerialName("file_description")
    val fileDescription: String,
    @SerialName("time_created")
    val timeCreated: Long,
    @SerialName("time_updated")
    val timeUpdated: Long,
    val visibility: Int,
    val flags: Long,
    @SerialName("workshop_file")
    val workshopFile: Boolean,
    @SerialName("workshop_accepted")
    val workshopAccepted: Boolean,
    @SerialName("show_subscribe_all")
    val showSubscribeAll: Boolean,
    @SerialName("num_comments_public")
    val numCommentsPublic: Int,
    @SerialName("num_comments_developer")
    val numCommentsDeveloper: Int? = 0,
    val banned: Boolean,
    @SerialName("ban_reason")
    val banReason: String = "",
    val banner: String,
    @SerialName("can_be_deleted")
    val canBeDeleted: Boolean,
    @SerialName("app_name")
    val appName: String,
    @SerialName("file_type")
    val fileType: Int,
    @SerialName("can_subscribe")
    val canSubscribe: Boolean,
    val subscriptions: Int,
    val favorited: Int,
    val followers: Int,
    @SerialName("lifetime_subscriptions")
    val lifetimeSubscriptions: Int,
    @SerialName("lifetime_favorited")
    val lifetimeFavorited: Int,
    @SerialName("lifetime_followers")
    val lifetimeFollowers: Int,
    @SerialName("lifetime_playtime")
    val lifetimePlaytime: String,
    @SerialName("lifetime_playtime_sessions")
    val lifetimePlaytimeSessions: String,
    val views: Int,
    @SerialName("num_children")
    val numChildren: Int,
    @SerialName("num_reports")
    val numReports: Int,
    val tags: List<Tag>? = null,
    val language: Int,
    @SerialName("maybe_inappropriate_sex")
    val maybeInappropriateSex: Boolean,
    @SerialName("maybe_inappropriate_violence")
    val maybeInappropriateViolence: Boolean,
    @SerialName("revision_change_number")
    val revisionChangeNumber: String,
    val revision: Int,
    @SerialName("ban_text_check_result")
    val banTextCheckResult: Int? = 0,
    val metadata: String? = "",
    @SerialName("available_revisions")
    val availableRevisions: List<Int>? = null,
    @SerialName("author_snapshots")
    val authorSnapshots: List<AuthorSnapshot>? = null
)

@Serializable
data class AuthorSnapshot(
    val timestamp: Long,
    @SerialName("game_branch_min")
    val gameBranchMin: String,
    @SerialName("game_branch_max")
    val gameBranchMax: String,
    val manifestid: String
)
