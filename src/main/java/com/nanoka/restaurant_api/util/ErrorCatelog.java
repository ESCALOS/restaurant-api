package com.nanoka.restaurant_api.util;

import lombok.Getter;

@Getter
public enum ErrorCatelog {


    USER_NOT_FOUND("ERR_USER_001", "User not found."),
    INVALID_USER("ERR_USER_002", "Invalid user parameters."),
    USER_USERNAME_ALREADY_EXISTS("ERR_USER_003", "Username already exists."),
    USER_DOCUMENT_NUMBER_ALREADY_EXISTS("ERR_USER_004", "Document Number already exists."),
    GENERIC_ERROR("ERR_GEN_001", "An unexpected error occurred"),
    ACCESS_DENIED("ERR_GEN_002","You do not have permission to access this resource."),
    UNAUTHORIZED("ERR_GEN_003","Authentication is required to access this resource."),
    BAD_CREDENTIALS("ERR_GEN_004", "Invalid password");

    private final String code;
    private final String message;

    ErrorCatelog(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
