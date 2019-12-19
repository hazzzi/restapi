package me.hazzzi.restapi.events

import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@Component
class EventValidator {

    fun validate(eventDto: EventDto, errors: Errors) {
        if (eventDto.basePrice != null && eventDto.maxPrice != null) {
            if (eventDto.basePrice > eventDto.maxPrice && eventDto.maxPrice > 0) {
                errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong")
                errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong")
            }

            val endEventDateTime = eventDto.endEventDateTime
            if (endEventDateTime.isBefore(eventDto.beginEventDateTime) ||
                endEventDateTime.isBefore(eventDto.closeEnrollmentDateTime) ||
                endEventDateTime.isBefore(eventDto.beginEnrollmentDateTime)) {
                errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong")
            }

            val beginEventDateTime = eventDto.beginEventDateTime
            if(beginEventDateTime.isAfter(eventDto.endEventDateTime) ||
                beginEventDateTime.isAfter(eventDto.closeEnrollmentDateTime)||
                beginEventDateTime.isAfter(eventDto.beginEnrollmentDateTime)) {
                errors.rejectValue("beginEventDateTime", "wrongValue", "BeginEventDateTime is wrong")
            }

            val closeEnrollmentDateTime = eventDto.closeEnrollmentDateTime
            if(closeEnrollmentDateTime.isBefore(eventDto.beginEventDateTime) ||
                closeEnrollmentDateTime.isBefore(eventDto.endEventDateTime) ||
                closeEnrollmentDateTime.isBefore(eventDto.beginEnrollmentDateTime)){
                errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "CloseEnrollmentDateTime is wrong")
            }
        }
    }
}