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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "GioHang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Giohang - Giohang_Sanpham
    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Giohang_Sanpham> gioHangSanPhams = new ArrayList<>();

    // GioHang - KhachHang
    @OneToOne(mappedBy = "gioHang", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    private KhachHang khachHang;

    // Thêm sản phẩm vào giỏ hàng với số lượng
    public void addSanPham(SanPham sanPham, int soLuong) {
        Giohang_Sanpham gioHangSanPham = new Giohang_Sanpham(this, sanPham, soLuong);
        gioHangSanPhams.add(gioHangSanPham);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeSanPham(SanPham sanPham) {
        gioHangSanPhams.removeIf(gioHangSanPham -> gioHangSanPham.getSanPham().equals(sanPham));
    }
}
