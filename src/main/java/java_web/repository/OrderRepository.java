package java_web.repository;

import java_web.entity.Order;
import java_web.entity.OrderStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository {

    private SessionFactory sessionFactory;

    public OrderRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Order order) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public int count() {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT COUNT(o) FROM Order o";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.getSingleResult().intValue();
        } finally {
            session.close();
        }
    }

    public double totalRevenue() {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT SUM(od.unitPrice * od.quantity) FROM OrderDetail od";
            Query<Double> query = session.createQuery(hql, Double.class);
            Double total = query.getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            session.close();
        }
    }

    public List<Order> findOrders(int page, int size) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "FROM Order o ORDER BY o.orderDate DESC";
            Query<Order> query = session.createQuery(hql, Order.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public double totalRevenueByCustomer(Long customerId) {
        Session session = sessionFactory.openSession();

        try {
            String hql = "SELECT SUM(od.unitPrice * od.quantity) FROM OrderDetail od WHERE od.order.customer.id = :customerId";
            Query<Double> query = session.createQuery(hql, Double.class)
                    .setParameter("customerId", customerId);
            Double total = query.getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            session.close();
        }
    }

    public List<Order> searchOrders(String keyword, String status, LocalDateTime from, LocalDateTime to, int page, int size) {
        Session session = sessionFactory.openSession();

        try {
            StringBuilder hql = new StringBuilder("FROM Order o WHERE 1=1");

            if (keyword != null && !keyword.isEmpty())
                hql.append(" AND o.recipientName LIKE :kw");

            if (status != null && !status.isEmpty())
                hql.append(" AND o.status = :st");

            if (from != null)
                hql.append(" AND o.orderDate >= :from");

            if (to != null)
                hql.append(" AND o.orderDate <= :to");

            hql.append(" ORDER BY o.orderDate DESC");

            Query<Order> query = session.createQuery(hql.toString(), Order.class);

            if (keyword != null && !keyword.isEmpty())
                query.setParameter("kw", "%" + keyword + "%");

            if (status != null && !status.isEmpty())
                query.setParameter("st", OrderStatus.valueOf(status));

            if (from != null)
                query.setParameter("from", from);

            if (to != null)
                query.setParameter("to", to);

            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public long countOrders(String keyword, String status, LocalDateTime from, LocalDateTime to) {
        Session session = sessionFactory.openSession();

        try {
            StringBuilder hql = new StringBuilder("SELECT COUNT(o) FROM Order o WHERE 1=1");

            if (keyword != null && !keyword.isEmpty())
                hql.append(" AND o.recipientName LIKE :kw");

            if (status != null && !status.isEmpty())
                hql.append(" AND o.status = :st");

            if (from != null)
                hql.append(" AND o.orderDate >= :from");

            if (to != null)
                hql.append(" AND o.orderDate <= :to");

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (keyword != null && !keyword.isEmpty())
                query.setParameter("kw", "%" + keyword + "%");

            if (status != null && !status.isEmpty())
                query.setParameter("st", OrderStatus.valueOf(status));

            if (from != null)
                query.setParameter("from", from);

            if (to != null)
                query.setParameter("to", to);

            return query.getSingleResult();
        } finally {
            session.close();
        }
    }

    public void updateStatus(int orderId, OrderStatus status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Order order = session.get(Order.class, orderId);
            if (order != null) {
                order.setStatus(status);
                session.update(order);
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