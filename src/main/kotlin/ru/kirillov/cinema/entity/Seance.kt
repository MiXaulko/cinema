package ru.kirillov.cinema.entity

import ru.kirillov.cinema.dto.SeanceDto
import ru.kirillov.cinema.dto.SeanceForCreatingDto
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.*
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Seance(var name: String,
             var startTime: LocalDateTime,
             var price: Int,
             var minimumProfit: Int,
             var enabledPrivileges: Boolean,
             var numberOfRows: Int,
             var numberOfColumns: Int) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "seance")
    lateinit var seatReservations: List<SeatReservation>

    fun toDto(): SeanceDto = SeanceDto(id, name, startTime, price, numberOfRows, numberOfColumns)

    fun toCreatingDto(): SeanceForCreatingDto = SeanceForCreatingDto(id, name, startTime, price, minimumProfit, enabledPrivileges, numberOfRows, numberOfColumns)
}