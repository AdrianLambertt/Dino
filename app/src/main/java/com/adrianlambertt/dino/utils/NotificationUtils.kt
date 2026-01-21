package com.adrianlambertt.dino.utils

    fun getRandomDelay(minMinutes: Int, maxMinutes: Int): Long {
        return (minMinutes..maxMinutes).random().toLong()
    }
