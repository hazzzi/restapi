package me.hazzzi.restapi.events

import java.time.LocalDateTime

data class EventDto(
    var name: String,
    var description: String,
    var beginEnrollmentDateTime: LocalDateTime,
    var closeEnrollmentDateTime: LocalDateTime,
    var beginEventDateTime: LocalDateTime,
    var endEventDateTime: LocalDateTime,
    var location: String? = null,
    var basePrice: Int? = null,
    var maxPrice: Int? = null,
    var limitOfEnrollment: Int
)