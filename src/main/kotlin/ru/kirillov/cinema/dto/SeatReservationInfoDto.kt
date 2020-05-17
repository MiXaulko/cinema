package ru.kirillov.cinema.dto

import java.time.LocalDateTime

class SeatReservationInfoDto(id: Long,
                             row: Int,
                             column: Int,
                             var realPrice: Float,
                             var name: String,
                             var startTime: LocalDateTime
) : SeatReservationDto(id, row, column)