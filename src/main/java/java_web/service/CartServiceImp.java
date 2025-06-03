package java_web.service;

import java_web.entity.Cart;
import java_web.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImp implements CartService{
    @Autowired
    private CartRepository cartRepo;

    @Override
    public Cart findByCustomerId(Long customerId) {
        return cartRepo.findByCustomerId(customerId);
    }

    @Override
    public void save(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public void update(Cart cart) {
        cartRepo.update(cart);
    }

    @Override
    public void delete(Cart cart, int itemId) {
        cartRepo.delete(cart, itemId);
    }
}
