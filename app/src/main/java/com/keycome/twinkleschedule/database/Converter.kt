package com.keycome.twinkleschedule.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    private val gSon = Gson()

    @TypeConverter
    fun dateRevert(dateString: String): Date {
        return Date(
            dateString.substring(0..4).toInt(),
            dateString.substring(5..6).toInt(),
            dateString.substring(7..8).toInt()
        )
    }

    @TypeConverter
    fun dateConverter(date: Date): String {
        val builder = StringBuilder().apply {
            append(date.year)
            append("-")
            append(if (date.month < 10) "0${date.month}" else date.month)
            append("-")
            append(if (date.dayOfMonth < 10) "0${date.dayOfMonth}" else date.dayOfMonth)
        }
        return builder.toString()
    }

    @TypeConverter
    fun timeLineRevert(timeLineString: String): Array<String> =
        gSon.fromJson(timeLineString, object : TypeToken<Array<String>>() {}.type)

    @TypeConverter
    fun timeLineConvert(timeLineArray: Array<String>): String =
        gSon.toJson(timeLineArray)

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