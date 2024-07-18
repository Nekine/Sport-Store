package vn.neekine.shoes_store_website.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.repository.ClientRepository;
import vn.neekine.shoes_store_website.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService{
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void createClient(KhachHang client) {
        this.clientRepository.save(client);
    }

    
}
