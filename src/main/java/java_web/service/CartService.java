package java_web.service;

import java_web.entity.Cart;

public interface CartService {
    Cart findByCustomerId(Long customerId);
    void save(Cart cart);
    void update(Cart cart);
    void delete(Cart cart, int itemId);
}
