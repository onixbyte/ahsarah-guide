package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.domain.entity.Modification;

import java.util.List;

public record ModificationResponse(
        Long id,
        Long firearmId,
        String name,
        String code,
        List<String> tags,
        String note,
        String author,
        String videoUrl
) {
    public static ModificationResponse from(Modification modification) {
        return new ModificationResponse(
                modification.getId(),
                modification.getFirearm().getId(),
                modification.getName(),
                modification.getCode(),
                modification.getTags(),
                modification.getNote(),
                modification.getAuthor(),
                modification.getVideoUrl()
        );
    }
}

