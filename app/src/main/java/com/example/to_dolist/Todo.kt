package com.example.to_dolist

import java.time.Instant

data class todo(
    var id: Int,
    var title: String,
    var createdAt: Long
)

fun getfaketodo2(): List<todo> {
    return listOf(
        todo(1, "do it", Instant.now().toEpochMilli()),
        todo(2, "complete it", Instant.now().toEpochMilli()),
        todo(3, "keep eye on it", Instant.now().toEpochMilli()),
        todo(4, "live", Instant.now().toEpochMilli()),
        todo(5, "dont die", Instant.now().toEpochMilli()),
    )
}
