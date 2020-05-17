package ru.kirillov.cinema.entity

import ru.kirillov.cinema.dto.SeatReservationDto
import ru.kirillov.cinema.dto.SeatReservationInfoDto
import javax.persistence.*
import javax.persistence.GenerationType.*

@Entity
class SeatReservation(var realPrice: Float,
                      @Column(name = "row_")
                      var row: Int,
                      @Column(name = "column_")
                      var column: Int,
                      @ManyToOne
                      @JoinColumn(name = "fk_user_id")
                      var user: User,
                      @ManyToOne
                      @JoinColumn(name = "fk_seance_id")
                      var seance: Seance) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

    fun toInfoDto(): SeatReservationInfoDto = SeatReservationInfoDto(id, row, column, realPrice, seance.name, seance.startTime)

    fun toDto(): SeatReservationDto = SeatReservationDto(id, row, column)
}