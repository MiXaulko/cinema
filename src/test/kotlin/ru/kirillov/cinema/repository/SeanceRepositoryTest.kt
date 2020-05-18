package ru.kirillov.cinema.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import ru.kirillov.cinema.entity.Seance
import java.time.LocalDateTime.now

@SpringBootTest(webEnvironment = NONE)
class SeanceRepositoryTest {
    @Autowired
    lateinit var seanceRepository: SeanceRepository

    fun insertRow(): Seance {
        val seance = Seance("seance", now().plusMinutes(10), 300, 30, false, 10, 10)
        return seanceRepository.save(seance)
    }

    @Test
    fun testInsert() {
        val seance = insertRow()

        assertNotEquals(seance.id, 0)
    }

    @Test
    fun testUpdate() {
        val newName = "name"
        val seance = insertRow()
        seance.name = newName
        seanceRepository.save(seance)

        assertEquals(seanceRepository.findById(seance.id).get().name, newName)
    }

    @Test
    fun testDelete() {
        val seance = insertRow()
        seanceRepository.delete(seance)

        assertFalse(seanceRepository.findById(seance.id).isPresent)
    }

    @Test
    fun testGetSeancesByStartTimeAfter() {
        val seance = insertRow()
        seanceRepository.save(seance)
        assertTrue(seanceRepository.getSeancesByStartTimeAfter(now()).isNotEmpty())
    }

}