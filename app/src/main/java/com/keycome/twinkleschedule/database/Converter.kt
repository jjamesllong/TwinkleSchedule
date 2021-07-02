package com.keycome.twinkleschedule.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keycome.twinkleschedule.model.Date
import com.keycome.twinkleschedule.model.Day

class Converter {
    private val gSon = Gson()

    @TypeConverter
    fun dateRevert(dateString: String): Date {
//        return Date(
//            dateString.substring(0..3).toInt(),
//            dateString.substring(5..6).toInt(),
//            dateString.substring(8..9).toInt()
//        )
        return gSon.fromJson(dateString, Date::class.java)
    }

    @TypeConverter
    fun dateConverter(date: Date): String {
//        val builder = StringBuilder().apply {
//            append(date.year)
//            append("-")
//            append(if (date.month < 10) "0${date.month}" else date.month)
//            append("-")
//            append(if (date.dayOfMonth < 10) "0${date.dayOfMonth}" else date.dayOfMonth)
//        }
//        return builder.toString()
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