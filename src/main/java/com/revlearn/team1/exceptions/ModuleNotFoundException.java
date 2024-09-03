package com.revlearn.team1.exceptions;

public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(Long moduleId) {
        super(String.format("Module by id %d not found.", moduleId));
    }
}
