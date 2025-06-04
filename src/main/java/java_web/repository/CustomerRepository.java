package java_web.repository;

import java_web.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    private SessionFactory sessionFactory;

    public CustomerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(customer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Customer findByUsername(String username) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Customer c WHERE c.username = :username";
            Query<Customer> query = session.createQuery(hql, Customer.class)
                    .setParameter("username", username);
            return query.getResultList().stream().findFirst().orElse(null);
        } finally {
            session.close();
        }
    }

    public Customer login(String username, String password) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Customer c WHERE c.username = :username AND c.password = :password";
            Query<Customer> query = session.createQuery(hql, Customer.class)
                    .setParameter("username", username)
                    .setParameter("password", password);
            return query.getResultList().stream().findFirst().orElse(null);
        } finally {
            session.close();
        }
    }

    public int count() {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT COUNT(c) FROM Customer c";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult().intValue();
        } finally {
            session.close();
        }
    }

    public List<Customer> getCustomers(String keyword, int page, int pageSize) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Customer WHERE username LIKE :kw ORDER BY id DESC";
            Query<Customer> query = session.createQuery(hql, Customer.class);
            query.setParameter("kw", "%" + keyword + "%");
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public long countCustomers(String keyword) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT COUNT(*) FROM Customer WHERE username LIKE :kw";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("kw", "%" + keyword + "%");
            return query.getSingleResult();
        } finally {
            session.close();
        }
    }

    public Customer findById(Long id) {
        Session session = sessionFactory.openSession();

        try {
            return session.get(Customer.class, id);
        } finally {
            session.close();
        }
    }

    public void updateStatus(Long id, boolean status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                customer.setStatus(status);
                session.update(customer);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void updateCustomer(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Customer existingCustomer = session.get(Customer.class, customer.getId());
            if (existingCustomer != null) {
                existingCustomer.setUsername(customer.getUsername());
                existingCustomer.setPassword(customer.getPassword());
                existingCustomer.setEmail(customer.getEmail());
                existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                existingCustomer.setStatus(customer.isStatus());
                session.update(existingCustomer);
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