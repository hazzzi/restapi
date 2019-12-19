package me.hazzzi.restapi.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.io.IOException
import java.util.function.Consumer


@JsonComponent
class ErrorsSerializer : JsonSerializer<Errors>() {
    @Throws(IOException::class)
    override fun serialize(errors: Errors, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeStartArray()
        errors.fieldErrors.forEach(Consumer { e: FieldError ->
            try {
                gen.writeStartObject()
                gen.writeStringField("field", e.field)
                gen.writeStringField("objectName", e.objectName)
                gen.writeStringField("code", e.code)
                gen.writeStringField("defaultMessage", e.defaultMessage)
                val rejectedValue = e.rejectedValue
                rejectedValue?.let {
                    gen.writeStringField("rejectedValue", it.toString())
                }
                gen.writeEndObject()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        })
        errors.globalErrors.forEach(Consumer { e: ObjectError ->
            try {
                gen.writeStartObject()
                gen.writeStringField("objectName", e.objectName)
                gen.writeStringField("code", e.code)
                gen.writeStringField("defaultMessage", e.defaultMessage)
                gen.writeEndObject()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        })
        gen.writeEndArray()
    }
}