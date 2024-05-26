package net.bobdb;

public class Product {
    public Integer id;
    String name;
    String description;
    String manufacturer;
    String year;
    String price;
    public Integer quantity;

    public int getId() {
        return id;
    }

    public void setQuantity(Integer q) {
        this.quantity = q;
    }
}
