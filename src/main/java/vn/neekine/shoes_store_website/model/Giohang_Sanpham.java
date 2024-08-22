package vn.neekine.shoes_store_website.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gioHang_sanPham")
public class Giohang_Sanpham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mối quan hệ với GioHang
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gioHang_id", nullable = false)
    private GioHang gioHang;

    // Mối quan hệ với SanPham
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sanPham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "soLuong", nullable = false)
    private Long soLuong;

    public Giohang_Sanpham(GioHang gioHang, SanPham sanPham, Long soLuong) {
        this.gioHang = gioHang;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public Giohang_Sanpham(){
    }
}
