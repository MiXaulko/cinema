package ru.kirillov.cinema.service

import ru.kirillov.cinema.entity.Seance
import java.time.LocalDateTime

interface AdminService {
    fun addSeance(seance: Seance)
    fun removeSeance(id: Long)
    fun rescheduleSeance(id: Long, time: LocalDateTime)
    fun enableBenefits(id: Long)
    fun disableBenefits(id: Long)
}