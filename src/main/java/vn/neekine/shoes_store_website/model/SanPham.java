package vn.neekine.shoes_store_website.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "SanPham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ten")
    private String tenSanPham;

    @Column(name = "kich_thuoc")
    private String size;

    @Column(name = "loai")
    private String loai;

    @Column(name = "so_luong")
    private Long soluong;

    @Column(name = "gia_nhap")
    private Long giaNhap;

    @Column(name = "gia_ban")
    private Long giaBan;

    @Column(name = "ngay_nhap_hang")
    private Date ngayNhapHang;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "khuyenMai_id")
    private KhuyenMai khuyenMai;

    // SanPham - Anh
    @OneToMany(mappedBy = "sanPham",cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    private List<Anh> photos;

    // SanPham - GioHang
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "sanPham_gioHang",
        joinColumns = @JoinColumn(name = "sanPham_id"),
        inverseJoinColumns = @JoinColumn(name = "gioHang_id")
    )
    private List<GioHang> gioHangs;

    // SanPham - NSX
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "sanPham_NSX",
        joinColumns = @JoinColumn(name = "sanPham_id"),
        inverseJoinColumns = @JoinColumn(name = "NSX_id")
    )
    @JsonIgnore // Ngăn chặn vòng lặp vô hạn
    private List<NhaSanXuat> nhaSanXuats;
    
    // them anh cua san pham
    public void addAnh(Anh photo){
        if(photos == null){
            photos = new ArrayList<>();
        }

        photos.add(photo);
    }

    // them gio hang
    public void addGioHang(GioHang gioHang){
        if(gioHangs == null){
            gioHangs = new ArrayList<>();;
        }

        gioHangs.add(gioHang);
    }

    // them nha san xuat
    public void addNSX(NhaSanXuat nhaSanXuat){
        if(nhaSanXuats == null){
            nhaSanXuats = new ArrayList<>();;
        }

        nhaSanXuats.add(nhaSanXuat);
    }

}
