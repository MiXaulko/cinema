package ru.kirillov.cinema.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.kirillov.cinema.entity.SeatReservation

interface SeatReservationRepository : JpaRepository<SeatReservation, Long> {
}