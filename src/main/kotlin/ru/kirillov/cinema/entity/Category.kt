package ru.kirillov.cinema.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.*
import javax.persistence.Id

@Entity
class Category(
        var name: String,
        var discount: Float) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long = 0
}