package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ModificationBatchDeleteRequest(
        @NotEmpty(message = "批量删除ID列表不能为空")
        List<@Positive(message = "ID必须为正数") Long> ids
) {
}

