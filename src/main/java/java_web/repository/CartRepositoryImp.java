package java_web.repository;

import java_web.entity.Cart;
import java_web.entity.CartItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepositoryImp implements CartRepository {

    private SessionFactory sessionFactory;

    public CartRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Cart findByCustomerId(Long customerId) {
        Session session = sessionFactory.openSession();

        Query<Cart> query = session.createQuery("FROM Cart c WHERE c.customer.id = :customerId", Cart.class);
        query.setParameter("customerId", customerId);
        List<Cart> results = query.getResultList();

        session.close();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void save(Cart cart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(cart);

        transaction.commit();
        session.close();
    }

    @Override
    public void update(Cart cart) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.update(cart);

        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Cart cart, int itemId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

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
        session.close();
    }
}