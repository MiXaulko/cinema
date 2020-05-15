package ru.kirillov.cinema.service.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import ru.kirillov.cinema.entity.Seance
import ru.kirillov.cinema.repository.SeanceRepository
import ru.kirillov.cinema.service.AdminService
import ru.kirillov.cinema.util.TIME_SHOULD_BE_IN_THE_FUTURE
import ru.kirillov.cinema.util.SEANCE_ALREADY_STARTED
import ru.kirillov.cinema.util.SEANCE_DOES_NOT_EXISTS
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Service
class AdminServiceImpl(val seanceRepository: SeanceRepository) : AdminService {

    override fun addSeance(seance: Seance) {
        checkTime(seance.startTime, TIME_SHOULD_BE_IN_THE_FUTURE)
        seanceRepository.save(seance)
    }

    @Transactional
    override fun removeSeance(id: Long) {
        val seance = getSeanceOrElseThrow(id)
        checkTime(seance.startTime, SEANCE_ALREADY_STARTED)

        seanceRepository.delete(seance)
    }

    @Transactional
    override fun rescheduleSeance(id: Long, time: LocalDateTime) {
        checkTime(time, TIME_SHOULD_BE_IN_THE_FUTURE)
        val seance = getSeanceOrElseThrow(id)
        checkTime(seance.startTime, SEANCE_ALREADY_STARTED)
        seance.startTime = time
    }

    @Transactional
    override fun enableBenefits(id: Long) {
        val seance = getSeanceOrElseThrow(id)
        seance.enabledPrivileges = true
    }

    @Transactional
    override fun disableBenefits(id: Long) {
        val seance = getSeanceOrElseThrow(id)
        seance.enabledPrivileges = false
    }

    private fun checkTime(time: LocalDateTime, message: String) {
        if (time < now()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, message)
        }
    }

    private fun getSeanceOrElseThrow(id: Long): Seance {
        return seanceRepository.findById(id)
                .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, SEANCE_DOES_NOT_EXISTS) }
    }
}