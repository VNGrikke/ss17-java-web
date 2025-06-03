package java_web.service;

import java_web.entity.Order;
import java_web.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImp implements OrderService{
    @Autowired
    private OrderRepository orderRepo;

    @Override
    public void save(Order order) {
        orderRepo.save(order);
    }
}
