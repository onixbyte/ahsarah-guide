package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ModificationBatchCreateRequest(
        @NotEmpty(message = "批量创建列表不能为空")
        List<@Valid ModificationRequest> modifications
) {
}

