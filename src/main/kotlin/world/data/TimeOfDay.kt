package com.aznos.world.data

enum class TimeOfDay(val time: Long) {
    SUNRISE(0L),
    NOON(6000L),
    SUNSET(12000L),
    MIDNIGHT(18000L);
}