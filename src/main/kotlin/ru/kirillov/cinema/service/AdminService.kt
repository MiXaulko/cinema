package ru.kirillov.cinema.service


import ru.kirillov.cinema.dto.SeanceForCreatingDto
import java.time.LocalDateTime

interface AdminService {
    fun addSeance(seanceDto: SeanceForCreatingDto): SeanceForCreatingDto
    fun removeSeance(id: Long)
    fun rescheduleSeance(id: Long, time: LocalDateTime): SeanceForCreatingDto
    fun enableBenefits(id: Long): SeanceForCreatingDto
    fun disableBenefits(id: Long): SeanceForCreatingDto
}