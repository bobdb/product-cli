package net.bobdb;

public class Product {
    Integer id;
    String name;
    String description;
    String manufacturer;
    String year;
    String price;
    Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setQuantity(Integer q) {
        this.quantity = q;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public String getYear() {
        return this.year;
    }

    public String getPrice() {
        return this.price;
    }
}
