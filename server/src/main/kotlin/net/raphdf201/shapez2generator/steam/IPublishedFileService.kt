package net.raphdf201.shapez2generator.steam

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://partner.steamgames.com/doc/webapi/IPublishedFileService#QueryFiles

class IPublishedFileService {
    companion object {
        const val url = "https://api.steampowered.com/IPublishedFileService/QueryFiles/v1/"
        val query = WorkshopItemQuery(
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
            returnTags = false,
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
