package ru.kirillov.cinema.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.kirillov.cinema.entity.Role

interface RoleRepository : JpaRepository<Role, Long> {
}