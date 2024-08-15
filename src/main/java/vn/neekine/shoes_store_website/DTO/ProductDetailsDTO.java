package vn.neekine.shoes_store_website.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ProductDetailsDTO {
    private Long id;
    private String loai;
    private String ten;
    private String namePathProduct;
    private String kich_thuoc;
    private Long so_luong;
    private Long gia_ban;
    private int phan_tram;
    private List<String> photoNames;

    public ProductDetailsDTO(Long id, String loai, String ten, String namePathProduct, String kich_thuoc, Long so_luong, Long gia_ban, int phan_tram, List<String> photoNames) {
        this.id = id;
        this.loai = loai;
        this.ten = ten;
        this.namePathProduct = namePathProduct;
        this.kich_thuoc = kich_thuoc;
        this.so_luong = so_luong;
        this.gia_ban = gia_ban;
        this.phan_tram = phan_tram;
        this.photoNames = photoNames;
    }
}
