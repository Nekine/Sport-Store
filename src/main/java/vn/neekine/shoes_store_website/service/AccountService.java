package vn.neekine.shoes_store_website.service;

import vn.neekine.shoes_store_website.model.Account;

public interface AccountService {
    public void createAccount(Account account);
    public boolean isFirstTimeLogin(String email);
}
