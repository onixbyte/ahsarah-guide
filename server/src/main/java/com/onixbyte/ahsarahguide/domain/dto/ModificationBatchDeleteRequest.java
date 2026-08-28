package com.onixbyte.ahsarahguide.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Request DTO for batch deletion of modifications by ID.
 *
 * @author zihluwang
 */
public record ModificationBatchDeleteRequest(
        @NotEmpty(message = "批量删除ID列表不能为空")
        List<@Positive(message = "ID必须为正数") Long> ids
) {
}

