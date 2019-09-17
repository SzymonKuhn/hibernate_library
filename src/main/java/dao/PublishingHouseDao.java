package dao;

import model.PublishingHouse;
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

public class PublishingHouseDao {

    public  boolean saveOrUpdate(PublishingHouse publishingHouse) {
        boolean success = false;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(publishingHouse);
            transaction.commit();
            success = true;
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return success;
    }

    public List<PublishingHouse> getAll() {
        List<PublishingHouse> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<PublishingHouse> criteriaQuery = cb.createQuery(PublishingHouse.class);
            Root<PublishingHouse> rootTable = criteriaQuery.from(PublishingHouse.class);
            criteriaQuery.select(rootTable);
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<PublishingHouse> getById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            PublishingHouse entity = session.get(PublishingHouse.class, id);
            return Optional.ofNullable(entity);
        }
    }

    public boolean delete(Long id) {
        Optional<PublishingHouse> optionalEntity = getById(id);

        if (optionalEntity.isPresent()) {
            delete(optionalEntity.get());
            return true;
        } else {
            return false;
        }
    }

    public void delete(PublishingHouse publishingHouse) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(publishingHouse);
            transaction.commit();
        }
    }


}
