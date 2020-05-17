package ru.kirillov.cinema.service.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.kirillov.cinema.dto.SeanceDto
import ru.kirillov.cinema.dto.SeatReservationDto
import ru.kirillov.cinema.dto.SeatReservationInfoDto
import ru.kirillov.cinema.entity.Category
import ru.kirillov.cinema.entity.Seance
import ru.kirillov.cinema.entity.SeatReservation
import ru.kirillov.cinema.repository.SeanceRepository
import ru.kirillov.cinema.repository.SeatReservationRepository
import ru.kirillov.cinema.repository.UserRepository
import ru.kirillov.cinema.service.ClientService
import ru.kirillov.cinema.util.*
import java.time.LocalDateTime.now
import java.util.stream.Collectors

@Service
class ClientServiceImpl(val seanceRepository: SeanceRepository,
                        val userRepository: UserRepository,
                        val seatReservationRepository: SeatReservationRepository) : ClientService {

    override fun getActiveSeances(): List<SeanceDto> {
        val seances = seanceRepository.getSeancesByStartTimeAfter(now())
        val dtos = seances
                .stream()
                .map { e -> e.toDto(e) }
                .collect(Collectors.toList())
        return dtos
    }

    override fun reserveTicket(idSeance: Long, row: Int, col: Int, login: String): SeatReservationInfoDto {
        val seance = seanceRepository.findById(idSeance)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, SEANCE_DOES_NOT_EXISTS) }
        val user = userRepository.findByLogin(login)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENT_NOT_FOUND_EXCEPTION) }

        if (col < 0 || row < 0 || col > seance.numberOfColumns || row > seance.numberOfRows) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_COORDINATES)
        }

        if (seatReservationRepository.findBySeance_IdAndRowAndColumn(idSeance, row, col).isPresent) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, PLACE_ALREADY_RESERVED)
        }

        val category = user.category

        val reservation = SeatReservation(seance.price.toFloat(), row, col, user, seance)

        val currentProfit = seatReservationRepository.findProfitBySeanceId(seance.id)

        /*
            Для социальных граждан бронирование билетов доступно всегда
            Для обычных - если осталось < 15 мин до начала, либо минимальная выгода с сеанса не достигнута
        */
        if (category.discount > 0) {
            updatePriceForSocial(category, seance, reservation)
        } else if ((currentProfit >= seance.minimumProfit) && now().plusMinutes(15).isBefore(seance.startTime)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, CANNOT_BUY_TICKET)
        }

        seatReservationRepository.save(reservation)
        return reservation.toInfoDto()
    }

    private fun updatePriceForSocial(category: Category, seance: Seance, reservation: SeatReservation) {
        if (seance.enabledPrivileges) {
            reservation.realPrice *= ((100 - category.discount) / 100)
        }
    }

    @Transactional
    override fun getReservedPlacesForSeance(idSeance: Long): List<SeatReservationDto> {
        val seance = seanceRepository.findById(idSeance)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, SEANCE_DOES_NOT_EXISTS) }
        val reservations = seance.seatReservations
                .stream()
                .map { e -> e.toDto() }
                .collect(Collectors.toList())
        return reservations
    }

    override fun getReservationInfo(idTicket: Long): SeatReservationInfoDto {
        val seatReservation = seatReservationRepository.findById(idTicket)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, RESERVATION_NOT_FOUNT) }
        return seatReservation.toInfoDto()
    }

    override fun cancelReservationTicket(id: Long, login: String) {
        val seatReservation = seatReservationRepository.findById(id)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, RESERVATION_NOT_FOUNT) }
        if (seatReservation.user.login != login) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, CANNOT_CANCEL_RESERVATION)
        }
        seatReservationRepository.delete(seatReservation)
    }

    @Transactional
    override fun getVisitedSeances(login: String): List<SeatReservationInfoDto> {
        val client = userRepository.findByLogin(login)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, CLIENT_NOT_FOUND_EXCEPTION) }
        val seances = client.seatReservations
                .stream()
                .filter { e -> e.seance.startTime.isBefore(now()) }
                .map { e -> e.toInfoDto() }
                .collect(Collectors.toList())
        return seances

    }

}