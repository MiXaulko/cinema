package ru.kirillov.cinema.dto

import java.time.LocalDateTime

open class SeanceDto(var id: Long,
                     var name: String,
                     var startTime: LocalDateTime,
                     var price: Int,
                     var numberOfRows: Int,
                     var numberOfColumns: Int)

