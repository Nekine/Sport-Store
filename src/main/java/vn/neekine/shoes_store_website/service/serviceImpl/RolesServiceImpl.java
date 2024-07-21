package vn.neekine.shoes_store_website.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.model.Role;
import vn.neekine.shoes_store_website.repository.RolesRepository;
import vn.neekine.shoes_store_website.service.RolesService;

@Service
public class RolesServiceImpl  implements RolesService{
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public void createRole(Role role) {
        this.rolesRepository.save(role);
    }

}
