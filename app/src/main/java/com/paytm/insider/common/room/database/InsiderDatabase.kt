package com.paytm.insider.common.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paytm.insider.common.room.dao.HomeDao
import com.paytm.insider.common.room.entity.RoomEvent
import com.paytm.insider.common.room.entity.RoomEventList
import com.paytm.insider.common.room.entity.RoomHome

@Database(
    entities = [RoomHome::class, RoomEventList::class, RoomEvent::class],
    version = 1,
    exportSchema = false
)
abstract class InsiderDatabase : RoomDatabase() {
    abstract fun getHomeDao(): HomeDao
}