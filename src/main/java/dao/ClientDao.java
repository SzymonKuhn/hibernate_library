package dao;

import model.BookLent;
import model.Client;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDao {

    public  boolean saveOrUpdate(Client client) {
        boolean success = false;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(client);
            transaction.commit();
            success = true;
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return success;
    }

    public List<Client> getAll() {
        List<Client> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteriaQuery = cb.createQuery(Client.class);
            Root<Client> rootTable = criteriaQuery.from(Client.class);
            criteriaQuery.select(rootTable);
            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Optional<Client> getById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Client entity = session.get(Client.class, id);
            return Optional.ofNullable(entity);
        }
    }

    public boolean delete(Long id) {
        Optional<Client> optionalEntity = getById(id);
        if (optionalEntity.isPresent()) {
            delete(optionalEntity.get());
            return true;
        } else {
            return false;
        }
    }

    public void delete(Client client) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(client);
            transaction.commit();
        }
    }

    public List<Client> getByName(String name) {
        List<Client> clients;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
            Root<Client> root = query.from(Client.class);
            query.select(root).where(criteriaBuilder.like(root.get("surname"), "%"+name+"%"));
            clients = new ArrayList<>(session.createQuery(query).getResultList());
        }
        return clients;
    }

    public Optional<Client> getByIdNumber(String idNumber) {
        Optional<Client> client;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
            Root<Client> root = query.from(Client.class);
            query.select(root).where(criteriaBuilder.like(root.get("idNumber"), idNumber));
            client = session.createQuery(query).uniqueResultOptional();
        }
        return client;
    }

    public List<Client> getMostActiveClients() {
        List<Client> clients;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Client> queryForClient = criteriaBuilder.createQuery(Client.class);
            Root<BookLent> rootBookLent = queryForClient.from(BookLent.class);
            Join<BookLent, Client> booklentJoinClient = rootBookLent.join("client");
            queryForClient.select(booklentJoinClient);
            queryForClient.groupBy(rootBookLent.get("client"));
            queryForClient.orderBy(criteriaBuilder.desc(criteriaBuilder.count(rootBookLent.get("client"))));
            clients = new ArrayList<>(session.createQuery(queryForClient).getResultList());
        }
        return clients;
    }
}
