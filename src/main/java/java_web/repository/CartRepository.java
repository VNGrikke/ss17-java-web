package java_web.repository;

import java_web.entity.Cart;

public interface CartRepository {
    Cart findByCustomerId(Long customerId);
    void save(Cart cart);
    void update(Cart cart);
    void delete(Cart cart, int itemId);
}
