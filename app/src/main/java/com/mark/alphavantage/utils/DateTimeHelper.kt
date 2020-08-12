package com.mark.alphavantage.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {
    private const val ISO_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

    fun getDateFormat(date: String): Long? {
        val df = SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault())
        return df.parse(date)?.time
    }
}