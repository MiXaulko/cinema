package ru.kirillov.cinema.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.kirillov.cinema.entity.Seance
import java.time.LocalDateTime

interface SeanceRepository : JpaRepository<Seance, Long> {
    fun getSeancesByStartTimeAfter(time: LocalDateTime): List<Seance>
}