package com.nanoka.restaurant_api.serie.domain.model;

public enum EnumTypeVoucher {
    FACTURA(1, "FACTURA"),
    BOLETA(3, "BOLETA");

    private final int id;
    private final String name;

    EnumTypeVoucher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}