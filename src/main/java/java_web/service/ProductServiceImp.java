package java_web.service;

import java_web.entity.Product;
import java_web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService{

    @Autowired
    private ProductRepository productRepo;

    @Override
    public List<Product> findProducts(int page, int size) {
        return productRepo.findProducts(page, size);
    }

    @Override
    public long countProducts() {
        return productRepo.countProducts();
    }

    @Override
    public Product findById(Long id) {
        return productRepo.findById(id);
    }
}
