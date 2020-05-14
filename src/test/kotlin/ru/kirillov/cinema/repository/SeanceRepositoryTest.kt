package ru.kirillov.cinema.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import ru.kirillov.cinema.entity.Seance
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = NONE)
class SeanceRepositoryTest {
    @Autowired
    lateinit var seanceRepository: SeanceRepository

    @Test
    fun testFindAll() = assertEquals(seanceRepository.findAll().size, 0)

    fun insertRow(): Seance {
        val seance = Seance("seance", LocalDateTime.now(), 300, 30, false, 10, 10);
        return seanceRepository.save(seance);
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

        assertEquals(seanceRepository.findById(seance.id).orElseThrow().name, newName)
    }

    @Test
    fun testDelete() {
        val seance = insertRow();
        seanceRepository.delete(seance)

        assertTrue(seanceRepository.findById(seance.id).isEmpty)
    }

}