package com.fullcycle.admin.catalogo.infrastructure.video.persistence

import com.fullcycle.admin.catalogo.domain.video.Rating
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class RatingConverter : AttributeConverter<Rating, String> {
    override fun convertToDatabaseColumn(attribute: Rating?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): Rating? {
        return when (dbData) {
            null -> null
            else -> Rating.valueOf(dbData)
        }
    }
}