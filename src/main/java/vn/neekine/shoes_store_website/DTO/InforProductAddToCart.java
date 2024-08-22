package vn.neekine.shoes_store_website.DTO;

import lombok.Data;

@Data
public class InforProductAddToCart {
    private String name;
    private String size;
    private Long quantity;
}
