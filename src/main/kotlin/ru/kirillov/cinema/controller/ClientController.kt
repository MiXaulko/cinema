package ru.kirillov.cinema.controller

import org.springframework.web.bind.annotation.*
import ru.kirillov.cinema.dto.SeanceDto
import ru.kirillov.cinema.dto.SeatReservationDto
import ru.kirillov.cinema.dto.SeatReservationInfoDto
import ru.kirillov.cinema.service.ClientService

@RestController
class ClientController(val clientService: ClientService) {

    @GetMapping("/rest/seance/active")
    fun getAllActiveSeances(): List<SeanceDto> {
        return clientService.getActiveSeances()
    }

    @PostMapping("/rest/seance/reserve")
    fun reserveTicket(@RequestBody seatReservationDto: SeatReservationDto, @RequestParam login: String): SeatReservationInfoDto {
        return clientService.reserveTicket(seatReservationDto.id, seatReservationDto.row, seatReservationDto.column, login)
    }

    @GetMapping("/rest/reservedPlaces")
    fun getReservedPlacesForSeance(id: Long): List<SeatReservationDto> {
        return clientService.getReservedPlacesForSeance(id)
    }

    @GetMapping("/rest/seance/info")
    fun getReservationInfo(id: Long): SeatReservationInfoDto {
        return clientService.getReservationInfo(id)
    }

    @DeleteMapping("/rest/seance/cancelReservation")
    fun cancelReservationTicket(id: Long, login: String) {
        clientService.cancelReservationTicket(id, login)
    }

    @GetMapping("/rest/seance/visited")
    fun getVisitedSeances(login: String): List<SeatReservationInfoDto> {
        return clientService.getVisitedSeances(login)
    }
}