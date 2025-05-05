package com.example.quizapp.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalDateTimeConverter {
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return try {
            value?.format(formatter)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return try {
            value?.let { LocalDateTime.parse(it, formatter) }
        } catch (e: DateTimeParseException) {
            null
        }
    }
}