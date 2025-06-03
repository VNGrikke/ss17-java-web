package java_web.service;

import java_web.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findProducts (int page, int size);
    long countProducts();
    Product findById(Long id);
}
