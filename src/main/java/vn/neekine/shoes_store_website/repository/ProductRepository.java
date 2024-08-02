package vn.neekine.shoes_store_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

       @Query("SELECT p FROM SanPham p " +
              "LEFT JOIN FETCH p.khuyenMai s " +
              "LEFT JOIN FETCH p.photos ph " +
              "LEFT JOIN FETCH p.nhaSanXuats nsx " +
              "WHERE (:brands IS NULL OR nsx.ten IN :brands) " +
              "AND (:sizes IS NULL OR p.size IN :sizes) " +
              "AND (:minPrices IS NULL OR :maxPrices IS NULL OR (" +
              "(p.giaBan * (1 - COALESCE(s.phanTram, 0) / 100) BETWEEN :minPrices AND :maxPrices)))")
       List<SanPham> findFilteredProducts(
              @Param("brands") List<String> brands,
              @Param("sizes") List<String> sizes,
              @Param("minPrices") List<Long> minPrices,
              @Param("maxPrices") List<Long> maxPrices);
}
