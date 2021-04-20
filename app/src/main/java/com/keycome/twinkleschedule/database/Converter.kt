package com.keycome.twinkleschedule.database

import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun dayRevert(dayString: String) = try {
        Day.valueOf(dayString)
    } catch (e: Exception) {
        throw IllegalArgumentException(e)
    }

    @TypeConverter
    fun dayConvert(day: Day): String = day.name

    @TypeConverter
    fun continuityRevert(continuityString: String) = try {
        Continuity.valueOf(continuityString)
    } catch (e: Exception) {
        throw IllegalArgumentException(e)
    }

    @TypeConverter
    fun continuityConvert(continuity: Continuity): String = continuity.name
}