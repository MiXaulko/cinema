package ru.kirillov.cinema.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.web.server.ResponseStatusException
import ru.kirillov.cinema.entity.Seance
import ru.kirillov.cinema.entity.SeatReservation
import ru.kirillov.cinema.entity.User
import ru.kirillov.cinema.repository.*
import ru.kirillov.cinema.util.*
import java.time.LocalDateTime.now

@SpringBootTest(webEnvironment = NONE)
class ClientServiceTest {

    @Autowired
    lateinit var clientService: ClientService

    @Autowired
    lateinit var seanceRepository: SeanceRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var seatReservationRepository: SeatReservationRepository

    fun addSeance(): Seance = seanceRepository.save(Seance("seance", now().plusHours(10), 100, 1000, true, 10, 10))

    fun insertUser(login: String): User {
        val category = categoryRepository.findById(1L).get()
        val role = roleRepository.findById(1L).get()
        val user = User(login, category, role)
        return userRepository.save(user)
    }

    @Test
    fun testGetActiveSeances() {
        addSeance()
        assertTrue(clientService.getActiveSeances().isNotEmpty())
    }

    @Test
    fun testReserveTicketForNotExistedSeance() {
        val exception = assertThrows<ResponseStatusException> {
            clientService.reserveTicket(-100, 1, 1, "notExistedUser")
        }
        assertEquals(exception.reason, SEANCE_DOES_NOT_EXISTS)
    }

    @Test
    fun testReserveTicketForNotExistedUser() {
        val seance = addSeance()
        val exception = assertThrows<ResponseStatusException> {
            clientService.reserveTicket(seance.id, 1, 1, "notExistedUser")
        }
        assertEquals(exception.reason, CLIENT_NOT_FOUND_EXCEPTION)
    }

    @Test
    fun testReserveTicketWithWrongCoordinates() {
        val seance = addSeance()
        val login = "user231"
        val user = insertUser(login)
        val exception = assertThrows<ResponseStatusException> {
            clientService.reserveTicket(seance.id, seance.numberOfRows + 1, seance.numberOfColumns + 1, login)
        }
        assertEquals(exception.reason, WRONG_COORDINATES)
    }

    @Test
    fun testReserveAlreadyReservedPlace() {
        val seance = addSeance()
        val user = insertUser("login")
        val login2 = "login2"
        val user2 = insertUser(login2)
        val row = 1
        val column = 1
        val seatReservation = SeatReservation(100f, row, column, user2, seance)
        seatReservationRepository.save(seatReservation)
        val exception = assertThrows<ResponseStatusException> {
            clientService.reserveTicket(seance.id, row, column, login2)
        }
        assertEquals(exception.reason, PLACE_ALREADY_RESERVED)
    }

    @Test
    fun testReservePlaceWhereMinimalProfitDoesNotReached() {
        val seance = addSeance()
        val user = insertUser("login0")
        val dto = clientService.reserveTicket(seance.id, 1, 1, user.login)
        assertEquals(dto.realPrice, seance.price.toFloat())
    }

    @Test
    fun testReserveForCommonBefore15Min() {
        val seance = seanceRepository.save(Seance("seance1", now().plusHours(10), 100, 0, true, 10, 10))
        val user = insertUser("login1")
        val exception = assertThrows<ResponseStatusException> {
            clientService.reserveTicket(seance.id, 1, 1, user.login)
        }
        assertEquals(exception.reason, CANNOT_BUY_TICKET)
    }

    @Test
    fun testReservePlaceForSocial() {
        val seance = seanceRepository.save(Seance("seance2", now().plusHours(10), 100, 0, true, 10, 10))
        val login = "login4"
        val category = categoryRepository.findById(2L).get()
        val role = roleRepository.findById(1L).get()
        val user = User(login, category, role)
        userRepository.save(user)
        val dto = clientService.reserveTicket(seance.id, 1, 1, user.login)
        assertNotEquals(dto.realPrice, seance.price.toFloat())
    }

    @Test
    fun testReservePlaceForSocialWithDisabledSocialMode() {
        val seance = seanceRepository.save(Seance("seance3", now().plusHours(10), 100, 0, false, 10, 10))
        val login = "login3"
        val category = categoryRepository.findById(2L).get()
        val role = roleRepository.findById(1L).get()
        val user = User(login, category, role)
        userRepository.save(user)
        val dto = clientService.reserveTicket(seance.id, 1, 1, user.login)
        assertEquals(dto.realPrice, seance.price.toFloat())
    }

    fun insertSeanceAndReserveOnePlace(): SeatReservation {
        val seance = addSeance()
        val user = insertUser("login")
        val seatReservation = SeatReservation(seance.price.toFloat(), 1, 1, user, seance)
        return seatReservationRepository.save(seatReservation)
    }

    @Test
    fun testGetReservedPlacesForSeance() {
        val reservation = insertSeanceAndReserveOnePlace()
        assertTrue(clientService.getReservedPlacesForSeance(reservation.seance.id).isNotEmpty())
    }

    @Test
    fun testGetVisitedSeances() {
        val seance = seanceRepository.save(Seance("seance", now().minusHours(10), 100, 1000, true, 10, 10))
        val login = "userWithVisitedSeance"
        val category = categoryRepository.findById(1L).get()
        val role = roleRepository.findById(1L).get()
        val user = User(login, category, role)
        userRepository.save(user)
        val seatReservation = SeatReservation(seance.price.toFloat(), 1, 1, user, seance)
        seatReservationRepository.save(seatReservation)
        assertTrue(clientService.getVisitedSeances(login).isNotEmpty())
    }

    @Test
    fun testCancelReservationTicket() {
        val seance = addSeance()
        val login = "login"
        val user = insertUser(login)
        val seatReservation = SeatReservation(seance.price.toFloat(), 1, 1, user, seance)
        seatReservationRepository.save(seatReservation)
        clientService.cancelReservationTicket(seatReservation.id, login)
        assertFalse(seatReservationRepository.findById(seatReservation.id).isPresent)
    }
}