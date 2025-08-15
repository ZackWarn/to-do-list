package com.example.to_dolist

import java.time.Instant
import java.util.Date

data class todo(
    var id:Int,
    var title:String,
    var createdAt: Date
)

fun getfaketodo2():List<todo>{

    return listOf<todo>(
        todo(1,"do it",Date.from(Instant.now())),
        todo(2,"complete it",Date.from(Instant.now())),
        todo(3,"keep eye on it",Date.from(Instant.now())),
        todo(4,"live",Date.from(Instant.now())),
        todo(5,"dont die",Date.from(Instant.now())),


        )
}