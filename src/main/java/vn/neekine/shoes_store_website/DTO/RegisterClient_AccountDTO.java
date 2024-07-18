package vn.neekine.shoes_store_website.DTO;

import lombok.Data;
import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.model.KhachHang;

@Data
public class RegisterClient_AccountDTO {
    private KhachHang client;
    private Account account;
}
