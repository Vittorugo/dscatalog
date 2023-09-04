package com.devsuperior.dscatalog.resources.exceptions;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends MessageError {
    private List<FieldMessage> errors = new ArrayList<>();

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
