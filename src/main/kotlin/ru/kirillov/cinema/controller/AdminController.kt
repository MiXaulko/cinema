package ru.kirillov.cinema.controller

import org.springframework.web.bind.annotation.*
import ru.kirillov.cinema.dto.SeanceForCreatingDto
import ru.kirillov.cinema.service.AdminService
import java.time.LocalDateTime

@RestController
class AdminController(val adminService: AdminService) {

    @PostMapping("/rest/seance")
    fun create(@RequestBody seanceDto: SeanceForCreatingDto): SeanceForCreatingDto {
        return adminService.addSeance(seanceDto)
    }

    @DeleteMapping("/rest/seance")
    fun remove(@RequestParam id: Long) {
        adminService.removeSeance(id)
    }

    @PatchMapping("/rest/seance/time")
    fun reschedule(@RequestParam id: Long, @RequestParam time: String): SeanceForCreatingDto {
        return adminService.rescheduleSeance(id, LocalDateTime.parse(time))
    }

    @PatchMapping("/rest/seance/benefits/enable")
    fun enableBenefits(@RequestParam id: Long): SeanceForCreatingDto {
        return adminService.enableBenefits(id)
    }

    @PatchMapping("/rest/seance/benefits/disable")
    fun disableBenefits(@RequestParam id: Long): SeanceForCreatingDto {
        return adminService.disableBenefits(id)
    }
}