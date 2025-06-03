package java_web.repository;

import java_web.entity.Customer;

public interface CustomerRepository {
    void save(Customer customer);
    Customer findByUsername(String username);
    Customer login(String username, String password);
}
