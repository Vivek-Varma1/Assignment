package com.assignment.project.customExceptionHandler;

public class ResourceNotFoundException extends RuntimeException {

    String resource;
    String fieldName;
    String field;
    Long fieldId;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resource, String fieldName, String field) {
        super(String.format("%s not found with %s: %s",resource,fieldName,field));
        this.resource = resource;
        this.fieldName = fieldName;
        this.field = field;
    }

    public ResourceNotFoundException(String resource, String field, Long fieldId) {
        super(String.format("%s not found with %s: %s",resource,field,fieldId));
        this.resource = resource;
        this.field = field;
        this.fieldId = fieldId;
    }
}
