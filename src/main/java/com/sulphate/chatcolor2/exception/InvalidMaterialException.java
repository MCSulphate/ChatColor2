package com.sulphate.chatcolor2.exception;

public class InvalidMaterialException extends RuntimeException {

    private final String invalidName;

    public InvalidMaterialException(String invalidName) {
        this.invalidName = invalidName;
    }

    public String getInvalidName() {
        return invalidName;
    }

}
