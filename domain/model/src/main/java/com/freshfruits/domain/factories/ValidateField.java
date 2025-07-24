package com.freshfruits.domain.factories;

import java.util.Optional;

public interface ValidateField {

    default Boolean validateId(String id) {
        return Optional.ofNullable(id).isPresent();
    }
}