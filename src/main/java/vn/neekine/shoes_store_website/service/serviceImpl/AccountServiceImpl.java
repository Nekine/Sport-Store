package vn.neekine.shoes_store_website.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.repository.AccountRepository;
import vn.neekine.shoes_store_website.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void createAccount(Account account) {
        this.accountRepository.save(account);
    }

    
}
