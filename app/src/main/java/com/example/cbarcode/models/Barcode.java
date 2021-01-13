package com.example.cbarcode.models;

public class Barcode {
    private int Order;
    private int Quantity;
    private String Barcode;
    public Barcode() {
    }

    public Barcode(int order, int quantity, String barcode) {
        Order = order;
        Quantity = quantity;
        Barcode = barcode;
    }
    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }
}
