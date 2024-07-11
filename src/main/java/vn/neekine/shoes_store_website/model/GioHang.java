package vn.neekine.shoes_store_website.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "photo")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Giohang - SanPham
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "sanPham_gioHang",
        joinColumns = @JoinColumn(name = "gioHang_id"),
        inverseJoinColumns = @JoinColumn(name = "sanPham_id")
    )
    private List<SanPham> sanPham_s;

    // GioHang - KhachHang
    @OneToOne(mappedBy = "gioHang", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    private KhachHang khachHang;

    // them san pham vao gio hang
    public void addSanPham(SanPham sanPham){
        if(sanPham_s == null){
            sanPham_s = new ArrayList<>();
        }

        sanPham_s.add(sanPham);
    }
}
