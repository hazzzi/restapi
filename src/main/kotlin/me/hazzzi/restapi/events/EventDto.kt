package me.hazzzi.restapi.events

import java.time.LocalDateTime
import javax.validation.constraints.Min

data class EventDto(
    var name: String,
    var description: String,
    var beginEnrollmentDateTime: LocalDateTime,
    var closeEnrollmentDateTime: LocalDateTime,
    var beginEventDateTime: LocalDateTime,
    var endEventDateTime: LocalDateTime,
    var location: String? = null,
    @Min(0)
    var basePrice: Int? = null,
    @Min(0)
    var maxPrice: Int? = null,
    @Min(0)
    var limitOfEnrollment: Int
)