package java_web.repository;

import java_web.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImp implements CustomerRepository {

    private SessionFactory sessionFactory;

    public CustomerRepositoryImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(customer);

        transaction.commit();
        session.close();
    }

    @Override
    public Customer findByUsername(String username) {
        Session session = sessionFactory.openSession();

        Query<Customer> query = session.createQuery("FROM Customer c WHERE c.username = :username", Customer.class);
        query.setParameter("username", username);
        List<Customer> result = query.getResultList();

        session.close();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Customer login(String username, String password) {
        Session session = sessionFactory.openSession();

        Query<Customer> query = session.createQuery("FROM Customer c WHERE c.username = :username AND c.password = :password", Customer.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<Customer> result = query.getResultList();

        session.close();
        return result.isEmpty() ? null : result.get(0);
    }
}