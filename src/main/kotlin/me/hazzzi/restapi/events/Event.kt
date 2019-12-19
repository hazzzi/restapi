package me.hazzzi.restapi.events

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Event(
    @Id
    @GeneratedValue
    var id: Int?,
    val name: String,
    val description: String,
    val beginEnrollmentDateTime: LocalDateTime,
    val closeEnrollmentDateTime: LocalDateTime,
    val beginEventDateTime: LocalDateTime,
    val endEventDateTime: LocalDateTime,
    val location: String? = null,
    val basePrice: Int? = null,
    val maxPrice: Int? = null,
    val limitOfEnrollment: Int,
    var offline: Boolean?,
    var free: Boolean?,
    @Enumerated(EnumType.STRING)
    var eventStatus: EventStatus = EventStatus.DRAFT
) {
    fun update() {
        this.free = this.basePrice == 0 && this.maxPrice == 0

        this.offline = !(this.location == null || this.location.isEmpty())
    }
}

