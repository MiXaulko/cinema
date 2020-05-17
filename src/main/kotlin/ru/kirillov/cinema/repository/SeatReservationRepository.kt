package ru.kirillov.cinema.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.kirillov.cinema.entity.SeatReservation
import java.util.*

interface SeatReservationRepository : JpaRepository<SeatReservation, Long> {
    fun findBySeance_IdAndRowAndColumn(seanceId: Long, row: Int, column: Int): Optional<SeatReservation>

    @Query("select COALESCE(sum(s.realPrice),0) from SeatReservation s where s.id = :seanceId")
    fun findProfitBySeanceId(seanceId: Long): Float
}