package com.freshfruits.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ResponseSave {
    private final Boolean status;
    private final String message;
}
