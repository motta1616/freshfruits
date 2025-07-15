package com.freshfruits.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CreateResponse {
    private String status;
    private String message;
}
