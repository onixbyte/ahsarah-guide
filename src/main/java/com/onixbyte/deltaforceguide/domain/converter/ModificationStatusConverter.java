package com.onixbyte.deltaforceguide.domain.converter;

import com.onixbyte.deltaforceguide.enumeration.ModificationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA attribute converter that maps {@link ModificationStatus} enum to/from its integer database representation.
 *
 * @author zihluwang
 */
@Converter
public class ModificationStatusConverter implements AttributeConverter<ModificationStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ModificationStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public ModificationStatus convertToEntityAttribute(Integer dbData) {
        return ModificationStatus.fromCode(dbData);
    }
}
