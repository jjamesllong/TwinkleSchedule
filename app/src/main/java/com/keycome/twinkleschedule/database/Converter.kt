package com.keycome.twinkleschedule.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keycome.twinkleschedule.record.interval.Date
import com.keycome.twinkleschedule.record.interval.Day
import com.keycome.twinkleschedule.record.timetable.Section

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
    fun dayRevert(dayString: String) = try {
        Day.valueOf(dayString)
    } catch (e: Exception) {
        throw IllegalArgumentException()
    }

    @TypeConverter
    fun dayConvert(day: Day): String = day.name

    @TypeConverter
    fun intListRevert(gSonString: String): List<Int> =
        gSon.fromJson(gSonString, object : TypeToken<List<Int>>() {}.type)

    @TypeConverter
    fun intListConvert(intList: List<Int>): String = gSon.toJson(intList)

    @TypeConverter
    fun sectionListConvert(sectionList: List<Section>): String = gSon.toJson(sectionList)

    @TypeConverter
    fun sectionListRevert(gson: String): List<Section> {
        return gSon.fromJson(gson, object : TypeToken<List<Section>>() {}.type)
    }
}