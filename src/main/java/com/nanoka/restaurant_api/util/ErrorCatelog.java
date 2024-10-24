package com.nanoka.restaurant_api.util;

import lombok.Getter;

@Getter
public enum ErrorCatelog {


    USER_NOT_FOUND("Usuario no encontrado"),
    CATEGORY_NOT_FOUND("Categoria no encontrada"),
    CATEGORY_ALREADY_EXIST("La categoría ya existe"),
    PRODUCT_NOT_FOUND("Produto no encontrado"),
    PRODUCT_ALREADY_EXIST("El produto ya existe"),
    INVALID_USER("Parámetros de usuario no válidos."),
    USER_USERNAME_ALREADY_EXISTS("El nombre de usuario ya existe."),
    USER_DOCUMENT_NUMBER_ALREADY_EXISTS("Otro usuario ya tiene este número de documento."),
    GENERIC_ERROR("Error inesperado"),
    ACCESS_DENIED("No tienes permiso para acceder a este recurso"),
    UNAUTHORIZED("Inicia sesión para acceder a este recurso"),
    BAD_CREDENTIALS("Contraseña inválida");

    private final String message;

    ErrorCatelog(String message) {
        this.message = message;
    }
}
