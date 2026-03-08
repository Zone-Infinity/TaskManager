package me.isoham.taskmanager.dao;

import me.isoham.taskmanager.config.HibernateUtil;
import me.isoham.taskmanager.model.Task;
import me.isoham.taskmanager.model.TaskStatus;
import me.isoham.taskmanager.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskDAOImpl implements TaskDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDAOImpl.class);

    @Override
    public boolean createTask(Task task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(task);
            tx.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("Error creating task", e);
            return false;
        }
    }

    @Override
    public List<Task> getTasksByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Task WHERE user = :user ORDER BY status, id";

            return session.createQuery(hql, Task.class).setParameter("user", user).getResultList();
        } catch (Exception e) {
            LOGGER.error("Error fetching tasks", e);
            return List.of();
        }
    }

    @Override
    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Task task = session.get(Task.class, taskId);
            if (task == null) return false;
            task.setStatus(status);
            tx.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("Error updating task", e);
            return false;
        }
    }

    @Override
    public boolean deleteTask(int taskId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Task task = session.get(Task.class, taskId);
            if (task == null) return false;
            session.remove(task);
            tx.commit();

            return true;
        } catch (Exception e) {
            LOGGER.error("Error deleting task", e);
            return false;
        }
    }
}
