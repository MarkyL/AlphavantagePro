package com.mark.alphavantage.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {

    val LONG_DATE_PATTERN = "dd.MM.yyyy"
    val ISO_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
    val SHORT_TIME_PATTERN = "HH:mm"
    val DATE_TIME_PATTERN = "dd-MM-yy HH:mm"
    val TEST_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy"

    // 2020-08-11 19:32:00

    fun getDateFormat(date: String): Long? {
        val df = SimpleDateFormat(ISO_DATE_PATTERN, Locale.getDefault())
        return df.parse(date)?.time
    }

    fun getTimeFromTimestamp(timestamp: Long): String? {
        val df = SimpleDateFormat(SHORT_TIME_PATTERN, Locale.getDefault())
        return df.parse(timestamp.toString())?.toString()
    }

    fun getDateFromTimestamp(timestamp: Long): String? {
        val df = SimpleDateFormat(TEST_PATTERN, Locale.getDefault())
        return df.parse(Date(timestamp).toString())?.toString()
    }
}