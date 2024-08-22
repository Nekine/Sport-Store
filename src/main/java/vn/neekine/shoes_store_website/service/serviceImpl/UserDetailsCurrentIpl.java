package vn.neekine.shoes_store_website.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.repository.AccountRepository;
import vn.neekine.shoes_store_website.service.UserDetailsService;

@Service
public class UserDetailsCurrentIpl implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException{
        Account account = accountRepository.findByEmail(username);
        if(account == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return account;
    }

}
