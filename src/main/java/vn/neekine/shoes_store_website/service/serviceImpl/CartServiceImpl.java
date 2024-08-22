package vn.neekine.shoes_store_website.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.DTO.InforProductAddToCart;
import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.model.GioHang;
import vn.neekine.shoes_store_website.model.Giohang_Sanpham;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.model.SanPham;
import vn.neekine.shoes_store_website.repository.CartRepository;
import vn.neekine.shoes_store_website.repository.ClientRepository;
import vn.neekine.shoes_store_website.repository.ProductRepository;
import vn.neekine.shoes_store_website.service.CartService;
import vn.neekine.shoes_store_website.service.ProductService;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService  productService;

    @Override
    public GioHang addProductToCart(InforProductAddToCart addToCart, KhachHang client) {
        GioHang cart = client.getGioHang();

        if(cart == null){
            cart = new GioHang();
            client.setGioHang(cart);
            this.cartRepository.save(cart);
            this.clientRepository.save(client);
        }

        SanPham product = this.productRepository.findByTenSanPhamAndSize(addToCart.getName(), addToCart.getSize());

        // nếu đã có sản phẩm đấy rồi thì chỉ cần tăng thêm số lượng
        for(Giohang_Sanpham item : cart.getGioHangSanPhams()){
            if(item.getSanPham().getTenSanPham().equals(addToCart.getName()) && item.getSanPham().getSize().equals(addToCart.getSize())){
                item.setSoLuong(item.getSoLuong() + addToCart.getQuantity());
                this.cartRepository.save(cart);
                return cart;
            }
        }

        // nếu chưa có sản phẩm
        cart.addSanPham(product, addToCart.getQuantity());
        this.cartRepository.save(cart);

        return cart;
    }

    @Override
    public List<ProductDetailsDTO> getAllProductsInCart(GioHang cart) {
        List<ProductDetailsDTO> products = new ArrayList<>();

        for(Giohang_Sanpham item : cart.getGioHangSanPhams()){
            SanPham product = item.getSanPham();
            ProductDetailsDTO productDTO = this.productService.searchProduct(product.getTenSanPham()).getFirst();

            products.add(new ProductDetailsDTO(product.getId(), product.getLoai(), product.getTenSanPham(), productDTO.getNamePathProduct(), product.getSize(), item.getSoLuong(), product.getGiaBan(), productDTO.getPhan_tram(), productDTO.getPhotoNames()));
        }

        return products;
    }
}
