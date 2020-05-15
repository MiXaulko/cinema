package ru.kirillov.cinema.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.web.server.ResponseStatusException
import ru.kirillov.cinema.entity.Seance
import ru.kirillov.cinema.repository.SeanceRepository
import ru.kirillov.cinema.util.TIME_SHOULD_BE_IN_THE_FUTURE
import ru.kirillov.cinema.util.SEANCE_ALREADY_STARTED
import ru.kirillov.cinema.util.SEANCE_DOES_NOT_EXISTS
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpringBootTest(webEnvironment = NONE)
class AdminServiceTest {
    @Autowired
    lateinit var adminService: AdminService

    @Autowired
    lateinit var seanceRepository: SeanceRepository

    fun insertSeance(): Seance {
        val seance = Seance("seance", now().plusMinutes(10), 300, 30, false, 10, 10)
        return seanceRepository.save(seance)
    }

    @Test
    fun testAddingSeance() {
        val seance = Seance("seance", now(), 300, 30, false, 10, 10)
        adminService.addSeance(seance)
        assertTrue(seanceRepository.findAll().size > 0)
    }

    @Test
    fun testAddingWrongSeance() {
        val seance = Seance("seance", now().minusHours(1), 300, 30, false, 10, 10)

        val exception = assertThrows<ResponseStatusException> {
            adminService.addSeance(seance)
        }
        assertEquals(exception.reason, TIME_SHOULD_BE_IN_THE_FUTURE)
    }

    @Test
    fun testRemovingStartedSeance() {
        val seance = Seance("seance", now(), 300, 30, false, 10, 10)
        seanceRepository.save(seance)
        val exception = assertThrows<ResponseStatusException> {
            adminService.removeSeance(seance.id)
        }
        assertEquals(exception.reason, SEANCE_ALREADY_STARTED)
    }

    @Test
    fun testRemovingNotExistSeance() {
        val exception = assertThrows<ResponseStatusException> {
            adminService.removeSeance(1L)
        }
        assertEquals(exception.reason, SEANCE_DOES_NOT_EXISTS)
    }

    @Test
    fun testCorrectRemoving() {
        val seance = Seance("seance", now().plusHours(1), 300, 30, false, 10, 10)
        seanceRepository.save(seance)
        adminService.removeSeance(seance.id)

        assertTrue(seanceRepository.findById(seance.id).isEmpty)
    }

    @Test
    fun testEnablingBenefits() {
        val seance = insertSeance()
        adminService.enableBenefits(seance.id)
        val seanceFromDb = seanceRepository.findById(seance.id).get()
        assertTrue(seanceFromDb.enabledPrivileges)
    }

    @Test
    fun testRescheduleTimeSeance() {
        val seance = insertSeance()
        val nowPlusHour = now().plusHours(1)
        adminService.rescheduleSeance(seance.id, nowPlusHour)
        val seanceFromDb = seanceRepository.findById(seance.id).get()
        assertThat(seanceFromDb.startTime).isEqualToIgnoringNanos(nowPlusHour)
    }
}