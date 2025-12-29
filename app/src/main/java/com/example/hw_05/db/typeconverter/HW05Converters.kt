package com.example.hw_05.db.typeconverter

import androidx.room.TypeConverter
import java.util.Date

class HW05Converters {
    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromLongToDate(timeInMillis: Long): Date {
        return Date(timeInMillis)
    }
}