package ru.kirillov.cinema.dto

import ru.kirillov.cinema.entity.Seance
import java.time.LocalDateTime

class SeanceForCreatingDto(
        id: Long,
        name: String,
        startTime: LocalDateTime,
        price: Int,
        var minimumProfit: Int,
        var enabledPrivileges: Boolean,
        numberOfRows: Int,
        numberOfColumns: Int


) : SeanceDto(id, name, startTime, price, numberOfRows, numberOfColumns) {
    fun toEntity(): Seance = Seance(name, startTime, price, minimumProfit, enabledPrivileges, numberOfRows, numberOfColumns)
}