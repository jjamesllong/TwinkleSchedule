package com.keycome.twinkleschedule.data.database.util

import com.keycome.twinkleschedule.core.record.timetable.*
import com.keycome.twinkleschedule.data.database.entity.*

object RecordConverter {

    fun fromCourse(course: Course): CourseEntity {
        return CourseEntity(
            course.id,
            course.masterScheduleId,
            course.title
        )
    }

    fun toCourse(entity: CourseEntity): Course {
        return Course(
            entity.id,
            entity.masterScheduleId,
            entity.title
        )
    }

    fun fromRoutine(routine: Routine): RoutineEntity {
        return RoutineEntity(
            routine.id,
            routine.masterScheduleId,
            routine.name,
            routine.startDate,
        )
    }

    fun toRoutine(entity: RoutineEntity): Routine {
        return Routine(
            entity.id,
            entity.masterScheduleId,
            entity.name,
            entity.startDate
        )
    }

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

    fun fromZone(zone: Zone): ZoneEntity {
        return ZoneEntity(
            zone.id,
            zone.masterRoutineId,
            zone.position,
            zone.startSecondInDay,
            zone.endSecondInDay
        )
    }

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