package java_web.repository;

import java_web.entity.Cart;
import java_web.entity.CartItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

    private SessionFactory sessionFactory;

    public CartRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Cart findByCustomerId(Long customerId) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Cart c WHERE c.customer.id = :customerId";
            Query<Cart> query = session.createQuery(hql, Cart.class)
                    .setParameter("customerId", customerId);
            return query.getResultList().stream().findFirst().orElse(null);
        } finally {
            session.close();
        }
    }

    public void save(Cart cart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(cart);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void update(Cart cart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.update(cart);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(Cart cart, int itemId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            CartItem itemToRemove = null;
            for (CartItem item : cart.getItems()) {
                if (item.getId() == itemId) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                cart.getItems().remove(itemToRemove);
                session.delete(itemToRemove);
                session.update(cart);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}