import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao {

    public  boolean saveOrUpdate(Author author) {
        boolean success = false;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.saveOrUpdate(author);

            transaction.commit();
            success = true;
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return success;
    }

    public List<Author> getAll() {
        List<Author> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<Author> criteriaQuery = cb.createQuery(Author.class);

            Root<Author> rootTable = criteriaQuery.from(Author.class);

            criteriaQuery.select(rootTable);

            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Optional<Author> getById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            Author entity = session.get(Author.class, id);

            return Optional.ofNullable(entity);
        }
    }

    // delete
    public void delete(Long id) {
        Optional<Author> optionalEntity = getById(id);

        if (optionalEntity.isPresent()) {
            delete(optionalEntity.get());
        } else {
            System.err.println("Nie udało się odnaleźć instancji");
        }
    }

    public void delete(Author author) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.delete(author);

            transaction.commit();
        }
    }

    public void addBookToAuthor (Author author, Book book) {
        author.addBook(book);
        saveOrUpdate(author);
    }

    public List<Author> getByName(String name) {
        List<Author> authors = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> query = criteriaBuilder.createQuery(Author.class);
            Root<Author> root = query.from(Author.class);

            query.select(root).where(criteriaBuilder.like(root.get("surname"), "%" + name + "%"));
            authors.addAll(session.createQuery(query).getResultList());
        }
        return authors;
    }
}
