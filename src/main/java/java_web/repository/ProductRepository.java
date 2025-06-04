package java_web.repository;

import java_web.entity.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private SessionFactory sessionFactory;

    public ProductRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Product> findProducts(int page, int size) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Product p";
            Query<Product> query = session.createQuery(hql, Product.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public long count() {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT COUNT(p) FROM Product p";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult();
        } finally {
            session.close();
        }
    }

    public Product findById(Long id) {
        Session session = sessionFactory.openSession();

        try {
            return session.get(Product.class, id);
        } finally {
            session.close();
        }
    }

    public void save(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(product);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void update(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.update(product);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(Product product) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            if (session.contains(product)) {
                session.delete(product);
            } else {
                Product managedProduct = session.get(Product.class, product.getId());
                if (managedProduct != null) {
                    session.delete(managedProduct);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Product> search(String keyword, int page, int size) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Product p WHERE p.productName LIKE :kw";
            Query<Product> query = session.createQuery(hql, Product.class)
                    .setParameter("kw", "%" + (keyword == null ? "" : keyword) + "%");
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public long countByKeyword(String keyword) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT COUNT(p) FROM Product p WHERE p.productName LIKE :kw";
            Query<Long> query = session.createQuery(hql, Long.class)
                    .setParameter("kw", "%" + (keyword == null ? "" : keyword) + "%");
            return query.getSingleResult();
        } finally {
            session.close();
        }
    }
}