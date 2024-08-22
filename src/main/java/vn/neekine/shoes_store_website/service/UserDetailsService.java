package vn.neekine.shoes_store_website.service;

import vn.neekine.shoes_store_website.model.Account;

public interface UserDetailsService {
    public Account loadUserByUsername(String username);
}
