package me.hazzzi.restapi.events

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Event(
    @Id
    @GeneratedValue
    var id: Int?,
    var name: String,
    var description: String,
    var beginEnrollmentDateTime: LocalDateTime,
    var closeEnrollmentDateTime: LocalDateTime,
    var beginEventDateTime: LocalDateTime,
    var endEventDateTime: LocalDateTime,
    var location: String? = null,
    var basePrice: Int? = null,
    var maxPrice: Int? = null,
    var limitOfEnrollment: Int,
    var offline: Boolean?,
    var free: Boolean?,
    @Enumerated(EnumType.STRING)
    var eventStatus: EventStatus = EventStatus.DRAFT
)

