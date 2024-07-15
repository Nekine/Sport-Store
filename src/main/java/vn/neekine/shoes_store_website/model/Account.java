package vn.neekine.shoes_store_website.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TaiKhoan")
public class Account {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    // TaiKhoan - KhachHang
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "khachHang_id")
    private KhachHang khachHang;

    // Account - Roles
    @OneToMany(mappedBy = "account",cascade = {CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.REFRESH})
    private List<Role> roles;
}
