package ru.kirillov.cinema.entity

import javax.persistence.*
import javax.persistence.GenerationType.*

@Entity
class SeatReservation(var realPrice: Float,
                      @Column(name = "row_")
                      var row: Int,
                      @Column(name = "column_")
                      var column: Int,
                      @ManyToOne
                      @JoinColumn(name = "fk_user_id")
                      var user: User,
                      @ManyToOne
                      @JoinColumn(name = "fk_seance_id")
                      var seance: Seance
) {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0
}