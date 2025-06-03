package java_web.repository;

import java_web.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findProducts (int page, int size);
    long countProducts();
    Product findById(Long id);
}
