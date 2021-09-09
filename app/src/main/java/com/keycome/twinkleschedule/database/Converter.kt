package com.keycome.twinkleschedule.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keycome.twinkleschedule.model.horizon.Date
import com.keycome.twinkleschedule.model.horizon.Day

class Converter {
    private val gSon = Gson()

    @TypeConverter
    fun dateRevert(dateString: String): Date {
        return gSon.fromJson(dateString, Date::class.java)
    }

    @TypeConverter
    fun dateConverter(date: Date): String {
        return gSon.toJson(date)
    }

    @TypeConverter
    fun timeLineRevert(timeLineString: String): Set<TimeLine> =
        gSon.fromJson(timeLineString, object : TypeToken<Set<TimeLine>>() {}.type)

    @TypeConverter
    fun timeLineConvert(timeLine: Set<TimeLine>): String =
        gSon.toJson(timeLine)

    @TypeConverter
    fun dayRevert(dayString: String) = try {
        Day.valueOf(dayString)
    } catch (e: Exception) {
        throw IllegalArgumentException()
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