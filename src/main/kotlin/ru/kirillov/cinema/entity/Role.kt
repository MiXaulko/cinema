package ru.kirillov.cinema.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.*
import javax.persistence.Id

@Entity
class Role(var name: String) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}