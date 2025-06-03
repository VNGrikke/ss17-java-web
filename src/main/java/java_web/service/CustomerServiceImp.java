package java_web.service;

import java_web.entity.Customer;
import java_web.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImp implements CustomerService{

    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public void save(Customer customer) {
        customerRepo.save(customer);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepo.findByUsername(username);
    }

    @Override
    public Customer login(String username, String password) {
        return customerRepo.login(username, password);
    }
}
