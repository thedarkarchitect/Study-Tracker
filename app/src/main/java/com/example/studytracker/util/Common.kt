package com.example.studytracker.util

import androidx.compose.ui.graphics.Color
import com.example.studytracker.ui.theme.Green
import com.example.studytracker.ui.theme.Orange
import com.example.studytracker.ui.theme.Purple
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class Priority(val title: String, val color: Color, val value: Int) {
    LOW(title = "Low", color = Green, value = 0),
    MEDIUM(title = "Medium", color = Orange, value = 1),
    HIGH(title = "High", color = Purple, value = 2);

    companion object {
        fun fromInt( value: Int) = entries.firstOrNull { it.value == value } ?: MEDIUM //this value will be feed a value that will help return a priority enum
    }
}

fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        Instant
            .ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } ?: LocalDate.now()
    return date.format(DateTimeFormatter.ofPattern("dd MM yyyy"))
}