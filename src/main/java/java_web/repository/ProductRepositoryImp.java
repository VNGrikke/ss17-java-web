package java_web.repository;

import java_web.entity.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImp implements ProductRepository {

    private SessionFactory sessionFactory;

    public ProductRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Product> findProducts(int page, int size) {
        Session session = sessionFactory.openSession();

        Query<Product> query = session.createQuery("FROM Product", Product.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        List<Product> products = query.getResultList();

        session.close();
        return products;
    }

    @Override
    public long countProducts() {
        Session session = sessionFactory.openSession();

        Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
        long count = query.getSingleResult();

        session.close();
        return count;
    }

    @Override
    public Product findById(Long id) {
        Session session = sessionFactory.openSession();

        Product product = session.get(Product.class, id);

        session.close();
        return product;
    }
}