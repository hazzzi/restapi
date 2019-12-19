package me.hazzzi.restapi.events

import java.time.LocalDateTime
import javax.validation.constraints.Min

data class EventDto(
    val name: String,
    val description: String,
    val beginEnrollmentDateTime: LocalDateTime,
    val closeEnrollmentDateTime: LocalDateTime,
    val beginEventDateTime: LocalDateTime,
    val endEventDateTime: LocalDateTime,
    val location: String? = null,
    @Min(0)
    val basePrice: Int? = null,
    @Min(0)
    val maxPrice: Int? = null,
    @Min(0)
    val limitOfEnrollment: Int
)