package com.parking.common.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidateException extends RuntimeException {

    private final List<String> errors;

    public ValidateException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public ValidateException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public ValidateException(String field, String message) {
        super(field + ": " + message);
        this.errors = new ArrayList<>();
        this.errors.add(field + ": " + message);
    }

    public static ValidateException of(String message) {
        return new ValidateException(message);
    }

    public static ValidateException of(List<String> errors) {
        return new ValidateException(errors);
    }
}
