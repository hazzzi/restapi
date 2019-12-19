package me.hazzzi.restapi.events

import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class EventValidator {

    fun validate(eventDto: EventDto, errors: Errors) {
        if (eventDto.basePrice != null && eventDto.maxPrice != null) {
            if (eventDto.basePrice > eventDto.maxPrice && eventDto.maxPrice > 0) {
                errors.reject("wrongPrices", "Values for prices are wrong")
            }

            val endEventDateTime = eventDto.endEventDateTime
            if (endEventDateTime.isBefore(eventDto.beginEventDateTime) ||
                endEventDateTime.isBefore(eventDto.closeEnrollmentDateTime) ||
                endEventDateTime.isBefore(eventDto.beginEnrollmentDateTime)) {
                errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong")
            }

            // TODO beginEventDateTime
            // TODO endEnrollmentDateTime
        }
    }
}