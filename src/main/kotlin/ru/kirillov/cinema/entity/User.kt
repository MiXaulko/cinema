package ru.kirillov.cinema.entity

import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.*

@Entity
class User(var login: String,
           @ManyToOne(fetch = LAZY)
           @JoinColumn(name = "fk_category_id")
           var category: Category,
           @ManyToOne(fetch = LAZY)
           @JoinColumn(name = "fk_role_id")
           var role: Role
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}