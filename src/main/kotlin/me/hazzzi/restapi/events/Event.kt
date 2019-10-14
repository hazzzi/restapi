package me.hazzzi.restapi.events

import java.time.LocalDateTime


data class Event(
        var id: Int,
        var name: String,
        var description: String,
        var beginEnrollmentDateTime: LocalDateTime,
        var closeEnrollmentDateTime: LocalDateTime,
        var beginEventDateTime: LocalDateTime,
        var endEventDateTime: LocalDateTime,
        var location: String? = "",
        var basePrice: Int? = 0,
        var maxPrice: Int? = 0,
        var limitOfEnrollment: Int,
        var offline: Boolean,
        var free: Boolean,
        var eventStatus: EventStatus
)

enum class EventStatus {
    DRAFT, PUBLISHED, BEGAN_ENROLLMENT, CLOSED_ENROLLMENT, STARTED, ENDED
}
