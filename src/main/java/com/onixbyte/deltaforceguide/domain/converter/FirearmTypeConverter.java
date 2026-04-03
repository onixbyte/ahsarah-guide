package com.onixbyte.deltaforceguide.domain.converter;

import com.onixbyte.deltaforceguide.enumeration.FirearmType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FirearmTypeConverter implements AttributeConverter<FirearmType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FirearmType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public FirearmType convertToEntityAttribute(Integer dbData) {
        return FirearmType.fromCode(dbData);
    }
}


