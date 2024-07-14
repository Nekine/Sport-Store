package vn.neekine.shoes_store_website.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "KhachHang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten")
    private String fullName;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    // KhachHang - GioHang
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gioHang_id")
    private GioHang gioHang;

    // KhachHang - TaiKhoan
    @OneToOne(mappedBy = "khachHang", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    private Account Account;
}
