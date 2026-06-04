package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.domain.entity.Modification;

import java.util.List;

/**
 * Response DTO for a modification record including accessories and tags.
 *
 * @author zihluwang
 */
public record ModificationResponse(
        Long id,
        Long firearmId,
        String name,
        String code,
        List<String> tags,
        List<AccessoryResponse> accessories,
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
                modification.getAccessories() == null
                        ? List.of()
                        : modification.getAccessories().stream().map(AccessoryResponse::from).toList(),
                modification.getNote(),
                modification.getAuthor(),
                modification.getVideoUrl()
        );
    }
}

