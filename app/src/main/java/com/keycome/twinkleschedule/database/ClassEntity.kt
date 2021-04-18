package com.keycome.twinkleschedule.database

data class ClassEntity(
    var title: String,
    var day: Day,
    var section: Section,
    var week: Week,
    var continuity: Continuity,
    var teacher: String,
    var classroom: String,
)

data class Week(var startWeek: Int, var endWeek: Int)

data class Section(var startSection: Int, var endSection: Int)

enum class Day { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

enum class Continuity { Continuous, Odd, Even }