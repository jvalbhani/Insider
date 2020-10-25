package com.paytm.insider.common.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "home")
data class RoomHome(
    @PrimaryKey val id: Long = 0,
    val tags: String?,
    val groups: String?,
    val sorters: String?,
    val popular: String?,
    val featured: String?
)

@Entity(tableName = "eventlist")
data class RoomEventList(
    val homeId: Long? = null,
    @PrimaryKey val id: Long = 0,
    val groupWiseList: String?,
    val categoryWiseList: String?
)

@Entity(tableName = "event", primaryKeys = ["id"])
data class RoomEvent(
    val eventListId: Long? = null,
    val id: String,
    val minShowStartTime: String?,
    val name: String?,
    val type: String?,
    val slug: String,
    val horizontalCoverImage: String?,
    val verticalCoverImage: String?,
    val city: String?,
    val venueId: String?,
    val venueName: String?,
    val venueDateString: String?,
    val isRsvp: Boolean?,
    val eventState: String?,
    val priceDisplayString: String?,
    val communicationStrategy: String?,
    val model: String?,
    val applicableFilters: String?,
    val popularityScore: String?,
    val purchaseVisibility: String?,
    val minPrice: String?
)

data class RoomEventListWithEvent(
    @Embedded val roomEventList: RoomEventList?,
    @Relation(
        entityColumn = "eventListId",
        parentColumn = "id",
        entity = RoomEvent::class
    ) val masterList: List<RoomEvent>?
)

data class RoomHomeWithEvents(
    @Embedded val roomHome: RoomHome,
    @Relation(
        entityColumn = "homeId",
        parentColumn = "id",
        entity = RoomEventList::class
    ) val roomEventListWithEvent: RoomEventListWithEvent?
)