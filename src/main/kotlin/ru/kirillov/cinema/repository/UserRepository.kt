package ru.kirillov.cinema.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.kirillov.cinema.entity.User
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): Optional<User>
}