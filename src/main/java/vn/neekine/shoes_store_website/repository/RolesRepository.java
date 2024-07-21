package vn.neekine.shoes_store_website.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.neekine.shoes_store_website.model.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long>{

}