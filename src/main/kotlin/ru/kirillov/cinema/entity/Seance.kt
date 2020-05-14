package ru.kirillov.cinema.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.*
import javax.persistence.Id

@Entity
class Seance(var name: String,
             var startTime: LocalDateTime,
             var price: Int,
             var minimumProfit: Int,
             var enabledPrivileges: Boolean,
             var numberOfRows: Int,
             var numberOfColumns: Int) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}