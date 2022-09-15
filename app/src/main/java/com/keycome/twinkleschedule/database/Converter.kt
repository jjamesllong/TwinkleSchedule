package com.keycome.twinkleschedule.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    private val gson = Gson()

    private val stringListTypeToken = object : TypeToken<List<String>>() {}

    private val intListTypeToken = object : TypeToken<List<String>>() {}

    @TypeConverter
    fun stringListRevert(json: String): List<String> =
        gson.fromJson(json, stringListTypeToken.type)

    @TypeConverter
    fun stringListConvert(data: List<String>): String = gson.toJson(data)

    @TypeConverter
    fun intListRevert(json: String): List<Int> =
        gson.fromJson(json, intListTypeToken.type)

    @TypeConverter
    fun intListConvert(data: List<Int>): String = gson.toJson(data)

}