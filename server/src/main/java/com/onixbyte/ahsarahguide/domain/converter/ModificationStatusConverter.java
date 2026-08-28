package com.onixbyte.ahsarahguide.domain.converter;

import com.onixbyte.ahsarahguide.enumeration.ModificationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

/**
 * JPA attribute converter that maps {@link ModificationStatus} enum to/from its integer database representation.
 *
 * @author zihluwang
 */
@Converter
public class ModificationStatusConverter implements AttributeConverter<ModificationStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ModificationStatus attribute) {
        return Optional.ofNullable(attribute)
                .map(ModificationStatus::getCode)
                .orElse(null);
    }

    @Override
    public ModificationStatus convertToEntityAttribute(Integer dbData) {
        return ModificationStatus.fromCode(dbData);
    }
}
