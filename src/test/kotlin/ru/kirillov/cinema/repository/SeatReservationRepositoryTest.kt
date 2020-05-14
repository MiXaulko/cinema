package ru.kirillov.cinema.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import ru.kirillov.cinema.entity.Category
import ru.kirillov.cinema.entity.Seance
import ru.kirillov.cinema.entity.SeatReservation
import ru.kirillov.cinema.entity.User
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = NONE)
class SeatReservationRepositoryTest {
    @Autowired
    lateinit var seatReservationRepository: SeatReservationRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var seanceRepository: SeanceRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Test
    fun testFindAll() = assertEquals(seatReservationRepository.findAll().size, 0)

    fun insertRow(): SeatReservation {
        val category = Category("30%", 30f);
        categoryRepository.save(category)
        val role = roleRepository.findById(2L).orElseThrow()
        val user = User("user", category, role)
        userRepository.save(user)

        val price = 500

        val seance = Seance("Seance", LocalDateTime.now(), price, 10, true, 10, 10)
        seanceRepository.save(seance)

        val seatReservation = SeatReservation((seance.price * category.discount) / 100, 1, 1, user, seance);
        return seatReservationRepository.save(seatReservation);
    }

    @Test
    fun testInsert(){
        val seatReservation = insertRow()
        assertNotEquals(seatReservation.id, 0)
    }

    @Test
    fun testUpdate() {
        val newRow = 10
        val seatReservation = insertRow()
        seatReservation.row = newRow
        seatReservationRepository.save(seatReservation)
        val afterUpdate = seatReservationRepository.findById(seatReservation.id).orElseThrow();
        assertEquals(afterUpdate.row, newRow)
    }

    @Test
    fun testDelete() {
        val seatReservation = insertRow()
        seatReservationRepository.delete(seatReservation)
        assertTrue(seatReservationRepository.findById(seatReservation.id).isEmpty)
    }
}