package ru.kirillov.cinema.service

import ru.kirillov.cinema.dto.SeanceDto
import ru.kirillov.cinema.dto.SeatReservationDto
import ru.kirillov.cinema.dto.SeatReservationInfoDto


interface ClientService {
    fun getActiveSeances(): List<SeanceDto>
    fun reserveTicket(idSeance: Long, row: Int, col: Int, login: String): SeatReservationInfoDto
    fun getReservedPlacesForSeance(idSeance: Long): List<SeatReservationDto>
    fun getReservationInfo(idTicket: Long): SeatReservationInfoDto
    fun cancelReservationTicket(id: Long, login: String)
    fun getVisitedSeances(login: String): List<SeatReservationInfoDto>
}