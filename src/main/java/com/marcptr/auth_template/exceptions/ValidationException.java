package com.marcptr.auth_template.exceptions;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, List<String>> fieldErrors;

    public ValidationException(Map<String, List<String>> fieldErrors) {
        super("Errores de validación");
        this.fieldErrors = fieldErrors;
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
