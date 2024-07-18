package vn.neekine.shoes_store_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.neekine.shoes_store_website.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>{

    
}
