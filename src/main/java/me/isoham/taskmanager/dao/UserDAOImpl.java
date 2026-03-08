package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.config.HibernateUtil;
import me.isoham.taskmanager.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public boolean createUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("Error creating user", e);
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User WHERE username = :username";
            User user = session.createQuery(hql, User.class).setParameter("username", username).uniqueResult();

            return Optional.ofNullable(user);
        } catch (Exception e) {
            LOGGER.error("Error finding user", e);
            return Optional.empty();
        }
    }
}
