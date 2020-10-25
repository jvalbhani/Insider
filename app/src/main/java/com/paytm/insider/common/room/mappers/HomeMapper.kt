package com.paytm.insider.common.room.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.datamodel.EventList
import com.paytm.insider.common.datamodel.Home
import com.paytm.insider.common.datamodel.SpecialEvent
import com.paytm.insider.common.room.entity.*

fun RoomHomeWithEvents.map(): Home {
    return Home(
        tags = roomHome.tags?.mapToList(),
        groups = roomHome.groups?.mapToList(),
        sorters = roomHome.sorters?.mapToHashMap(),
        list = roomEventListWithEvent?.mapToEventList(),
        popular = roomHome.popular?.mapToSpecialEvents(),
        featured = roomHome.featured?.mapToSpecialEvents()
    )
}

fun Home.map(): RoomHomeWithEvents {
    val roomHome = RoomHome(
        tags = tags?.toJson(),
        groups = groups?.toJson(),
        sorters = sorters?.toJson(),
        popular = popular?.toJson(),
        featured = featured?.toJson()
    )
    return RoomHomeWithEvents(
        roomEventListWithEvent = list?.mapToEventList(roomHome.id),
        roomHome = roomHome
    )
}

fun RoomEvent.map(): Event {
    return Event(
        id = id,
        city = city,
        slug = slug,
        type = type,
        name = name,
        model = model,
        isRsvp = isRsvp,
        venueId = venueId,
        minPrice = minPrice,
        venueName = venueName,
        eventState = eventState,
        popularityScore = popularityScore,
        venueDateString = venueDateString,
        purchaseVisibility = purchaseVisibility,
        minShowStartTime = minShowStartTime,
        priceDisplayString = priceDisplayString,
        verticalCoverImage = verticalCoverImage,
        horizontalCoverImage = horizontalCoverImage,
        communicationStrategy = communicationStrategy,
        applicableFilters = applicableFilters?.mapToList()
    )
}

fun Event.map(eventListId: Long): RoomEvent {
    return RoomEvent(
        eventListId = eventListId,
        id = id,
        city = city,
        slug = slug,
        type = type,
        name = name,
        model = model,
        isRsvp = isRsvp,
        venueId = venueId,
        minPrice = minPrice,
        venueName = venueName,
        eventState = eventState,
        popularityScore = popularityScore,
        venueDateString = venueDateString,
        minShowStartTime = minShowStartTime,
        purchaseVisibility = purchaseVisibility,
        priceDisplayString = priceDisplayString,
        verticalCoverImage = verticalCoverImage,
        horizontalCoverImage = horizontalCoverImage,
        communicationStrategy = communicationStrategy,
        applicableFilters = applicableFilters?.toJson()
    )
}

fun List<RoomEvent>.mapToHashMap(): HashMap<String, Event> {
    val map = HashMap<String, Event>()
    forEach { map[it.slug] = it.map() }
    return map
}

fun HashMap<String, Event>.mapToList(eventListId: Long): List<RoomEvent> {
    return map { it.value.map(eventListId) }
}

fun RoomEventListWithEvent.mapToEventList(): EventList {
    return EventList(
        masterList = masterList?.mapToHashMap(),
        categorywiseList = roomEventList?.categoryWiseList?.mapToHashMap(),
        groupwiseList = roomEventList?.groupWiseList?.mapToHashMap()
    )
}

fun EventList.mapToEventList(homeId: Long): RoomEventListWithEvent {
    val roomEventList = RoomEventList(
        homeId = homeId,
        categoryWiseList = categorywiseList?.toJson(),
        groupWiseList = groupwiseList?.toJson()
    )
    return RoomEventListWithEvent(
        masterList = masterList?.mapToList(roomEventList.id),
        roomEventList = roomEventList
    )
}

fun String.mapToSpecialEvents(): List<SpecialEvent>? {
    return Gson().fromJson(this, Array<SpecialEvent>::class.java)?.toList()
}

fun <T> String.mapToList(): List<T> {
    return Gson().fromJson(this, (object : TypeToken<List<T>>() {}).type)
}

fun <T> List<T>.toJson(): String {
    return Gson().toJson(this)
}

fun <T> String.mapToHashMap(): HashMap<String, List<T>> {
    return Gson().fromJson(this, (object : TypeToken<HashMap<String, List<T>>>() {}).type)
}

fun <T> HashMap<String, List<T>>.toJson(): String {
    return Gson().toJson(this)
}
