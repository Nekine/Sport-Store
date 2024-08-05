package vn.neekine.shoes_store_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.neekine.shoes_store_website.model.SanPham;

@Repository
public interface ProductRepository extends JpaRepository<SanPham, Long>{

       @Query("SELECT p FROM SanPham p " +
              "LEFT JOIN FETCH p.khuyenMai s " +
              "LEFT JOIN FETCH p.photos ph")
       List<SanPham> findAllProducts();


       @Query("SELECT p FROM SanPham p " +
              "LEFT JOIN FETCH p.khuyenMai s " +
              "LEFT JOIN FETCH p.photos ph " + 
              "WHERE p.tenSanPham LIKE ?1%")
       List<SanPham> searchProductByName(String name);

}
