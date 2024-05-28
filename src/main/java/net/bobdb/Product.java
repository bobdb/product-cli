package net.bobdb;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    Integer id;
    String name;
    String description;
    String manufacturer;
    String year;
    String price;
    Integer quantity;
}
