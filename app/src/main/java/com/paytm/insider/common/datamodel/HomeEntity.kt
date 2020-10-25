package com.paytm.insider.common.datamodel

import com.google.gson.annotations.SerializedName

data class Home(
    val tags: List<String>?,
    val groups: List<String>?,
    val sorters: HashMap<String, List<Sorter?>>?,
    val list: EventList?,
    val popular: List<SpecialEvent>?,
    val featured: List<SpecialEvent>?
)

data class SpecialEvent(val slug: String)

data class EventList(
    val masterList: HashMap<String, Event>?,
    val groupwiseList: HashMap<String, List<String>>?,
    val categorywiseList: HashMap<String, List<String>>?
)

data class Event(
    @SerializedName("_id") val id: String,
    val name: String?,
    val type: String?,
    val slug: String,
    val city: String?,
    val model: String?,
    @SerializedName("min_show_start_time") val minShowStartTime: String?,
    @SerializedName("horizontal_cover_image") val horizontalCoverImage: String?,
    @SerializedName("vertical_cover_image") val verticalCoverImage: String?,
    @SerializedName("venue_id") val venueId: String?,
    @SerializedName("venue_name") val venueName: String?,
    @SerializedName("venue_date_string") val venueDateString: String?,
    @SerializedName("is_rsvp") val isRsvp: Boolean?,
    @SerializedName("event_state") val eventState: String?,
    @SerializedName("price_display_string") val priceDisplayString: String?,
    @SerializedName("communication_strategy") val communicationStrategy: String?,
    @SerializedName("applicable_filters") val applicableFilters: List<String>?,
    @SerializedName("popularity_score") val popularityScore: String?,
    @SerializedName("purchase_visibility") val purchaseVisibility: String?,
    @SerializedName("min_price") val minPrice: String?
)

data class Sorter(
    val key: String,
    val display: String,
    val type: String
)