package dao;

import model.BookLent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookLentDao {

    public  boolean saveOrUpdate(BookLent bookLent) {

        boolean success = false;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(bookLent);
            transaction.commit();
            success = true;
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return success;
    }

    public List<BookLent> getAll() {
        List<BookLent> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BookLent> criteriaQuery = cb.createQuery(BookLent.class);
            Root<BookLent> rootTable = criteriaQuery.from(BookLent.class);
            criteriaQuery.select(rootTable);
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Optional<BookLent> getById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            BookLent entity = session.get(BookLent.class, id);
            return Optional.ofNullable(entity);
        }
    }

    public boolean delete(Long id) {
        Optional<BookLent> optionalEntity = getById(id);

        if (optionalEntity.isPresent()) {
            delete(optionalEntity.get());
            return true;
        } else {
            return false;
        }
    }

    public void delete(BookLent bookLent) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(bookLent);
            transaction.commit();
        }
    }

    public List<BookLent> getBookLentNotReturned (Long clientId) {
        List<BookLent> bookLents;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<BookLent> query = criteriaBuilder.createQuery(BookLent.class);
            Root<BookLent> root = query.from(BookLent.class);
            query.select(root).where(criteriaBuilder.equal(root.get("client"), clientId), criteriaBuilder.isNull(root.get("dateReturned")));
            bookLents = new ArrayList<>(session.createQuery(query).getResultList());
        }
        return bookLents;
    }
}
