package net.bobdb;

public class Product {
    public Integer id;
    String name;
    String description;
    String manufacturer;
    String year;
    String price;
    public int quantity;

    public int getId() {
        return id;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }
}
