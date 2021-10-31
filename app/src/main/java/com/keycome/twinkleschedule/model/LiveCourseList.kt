package com.keycome.twinkleschedule.model

import androidx.lifecycle.LiveData
import com.keycome.twinkleschedule.model.horizon.Day
import com.keycome.twinkleschedule.model.sketch.Course
import com.keycome.twinkleschedule.model.sketch.CourseField

class LiveCourseList(
    private val parentScheduleId: CourseField.ParentScheduleId,
    courseList: MutableList<Course> = mutableListOf(
        Course(
            courseId = System.currentTimeMillis(),
            parentScheduleId = parentScheduleId.parentScheduleId,
            title = "",
            day = Day.Monday,
            section = (1..2).toList(),
            week = (1..18).toList(),
            teacher = "",
            classroom = ""
        )
    )
) : LiveData<MutableList<Course>>(courseList) {

    fun updateField(field: CourseField, index: Int) {

        value[index] = when (field) {

            is CourseField.Classroom -> value[index].copy(classroom = field.classroom)

            is CourseField.CourseId -> value[index].copy(courseId = field.courseId)

            is CourseField.FDay -> value[index].copy(day = field.day)

            is CourseField.ParentScheduleId ->
                value[index].copy(parentScheduleId = field.parentScheduleId)

            is CourseField.Section -> value[index].copy(section = field.section)

            is CourseField.Teacher -> value[index].copy(teacher = field.teacher)

            is CourseField.Title -> {
                for (i in value.indices) {
                    value[i] = value[i].copy(title = field.title)
                }
                value[index]
            }

            is CourseField.Week -> value[index].copy(week = field.week)
        }

        value = value

    }

    override fun getValue(): MutableList<Course> {
        return super.getValue()!!
    }

    override fun setValue(value: MutableList<Course>) {
        super.setValue(value)
    }

    fun addCourse(): Int {
        value.add(
            Course(
                courseId = System.currentTimeMillis(),
                parentScheduleId = parentScheduleId.parentScheduleId,
                title = if (value.isNotEmpty()) value[0].title else "",
                day = Day.Monday,
                section = (1..2).toList(),
                week = (1..18).toList(),
                teacher = "",
                classroom = ""
            )
        )
        value = value
        return value.size
    }

    fun deleteCourse(index: Int) {
        value.removeAt(index)
        value = value
    }

    fun checkFieldRight(): Boolean {
        for (c in value) {
            if (c.title == "")
                return false
        }
        return true
    }
}