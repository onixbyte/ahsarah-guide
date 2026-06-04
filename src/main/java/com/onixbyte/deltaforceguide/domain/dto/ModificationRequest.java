package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

/**
 * Request DTO for creating or updating a modification.
 *
 * @author zihluwang
 */
public record ModificationRequest(
        @NotNull(message = "武器ID不能为空")
        @Positive(message = "武器ID必须为正数")
        Long firearmId,
        @NotBlank(message = "改装名称不能为空")
        String name,
        @NotBlank(message = "改装代码不能为空")
        String code,
        List<@NotBlank(message = "标签不能为空") String> tags,
        List<@Valid AccessoryRequest> accessories,
        String note,
        String author,
        String videoUrl
) {
    public List<String> tags() {
        return tags == null ? new ArrayList<>() : tags;
    }

    public List<AccessoryRequest> accessories() {
        return accessories == null ? new ArrayList<>() : accessories;
    }
}

