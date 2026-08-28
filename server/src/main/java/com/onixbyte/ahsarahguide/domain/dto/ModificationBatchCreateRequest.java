package com.onixbyte.ahsarahguide.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Request DTO for batch creation of modifications.
 *
 * @author zihluwang
 */
public record ModificationBatchCreateRequest(
        @NotEmpty(message = "批量创建列表不能为空")
        List<@Valid ModificationRequest> modifications
) {
}

