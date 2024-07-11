package vn.neekine.shoes_store_website.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Khuyen_mai")
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Phan_tram")
    private int phanTram;

    @Column(name = "Ten_khuyen_mai")
    private String tenKhuyenMai;

    @Column(name = "Ngay_bat_dau")
    private Date ngayBatDau;

    @Column(name = "Ngay_ket_thuc")
    private Date ngayKetThuc;

    @OneToMany(mappedBy = "khuyenMai",cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    private List<SanPham> sanPhams;
    
}
