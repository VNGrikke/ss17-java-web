package java_web.service;

import java_web.entity.Customer;

public interface CustomerService {
    void save(Customer customer);
    Customer findByUsername(String username);
    Customer login(String username, String password);
}
