package com.keycome.twinkleschedule.data.database.util

import androidx.room.TypeConverter
import com.keycome.twinkleschedule.core.record.timetable.*
import com.keycome.twinkleschedule.data.database.entity.*

class RecordConverter {

    @TypeConverter
    fun fromCourse(course: Course): CourseEntity {
        return CourseEntity(
            course.id,
            course.masterScheduleId,
            course.title
        )
    }

    @TypeConverter
    fun toCourse(entity: CourseEntity): Course {
        return Course(
            entity.id,
            entity.masterScheduleId,
            entity.title
        )
    }

    @TypeConverter
    fun fromRoutine(routine: Routine): RoutineEntity {
        return RoutineEntity(
            routine.id,
            routine.masterScheduleId,
            routine.name,
            routine.startDate,
        )
    }

    @TypeConverter
    fun toRoutine(entity: RoutineEntity): Routine {
        return Routine(
            entity.id,
            entity.masterScheduleId,
            entity.name,
            entity.startDate
        )
    }

    @TypeConverter
    fun fromSchedule(schedule: Schedule): ScheduleEntity {
        return ScheduleEntity(
            schedule.id,
            schedule.name,
            schedule.startDate,
            schedule.sectionsPerDay,
            schedule.daysPerWeek,
            schedule.weeksPerTerm
        )
    }

    @TypeConverter
    fun toSchedule(entity: ScheduleEntity): Schedule {
        return Schedule(
            entity.id,
            entity.name,
            entity.startDate,
            entity.sectionsPerDay,
            entity.daysPerWeek,
            entity.weeksPerTerm
        )
    }

    @TypeConverter
    fun fromSection(section: Section): SectionEntity {
        return SectionEntity(
            section.id,
            section.masterCourseId,
            section.teacher,
            section.classroom,
            section.startOfDay,
            section.endOfDay,
            section.dayOfWeek,
            section.weekOfTerm
        )
    }

    @TypeConverter
    fun toSection(entity: SectionEntity): Section {
        return Section(
            entity.id,
            entity.masterCourseId,
            entity.teacher,
            entity.classroom,
            entity.startOfDay,
            entity.endOfDay,
            entity.dayOfWeek,
            entity.weekOfTerm
        )
    }

    @TypeConverter
    fun fromZone(zone: Zone): ZoneEntity {
        return ZoneEntity(
            zone.id,
            zone.masterRoutineId,
            zone.position,
            zone.startSecondInDay,
            zone.endSecondInDay
        )
    }

    @TypeConverter
    fun toZone(entity: ZoneEntity): Zone {
        return Zone(
            entity.id,
            entity.masterRoutineId,
            entity.position,
            entity.startSecondInDay,
            entity.endSecondInDay
        )
    }
}