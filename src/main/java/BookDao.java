import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.EntityType;
import java.time.LocalDate;
import java.util.*;

public class BookDao {

    public boolean saveOrUpdate(Book book) {
        boolean success = false;
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.saveOrUpdate(book);

            transaction.commit();
            success = true;
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return success;
    }

    public List<Book> getAll() {
        List<Book> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);

            Root<Book> rootTable = criteriaQuery.from(Book.class);

            criteriaQuery.select(rootTable);

            list.addAll(session.createQuery(criteriaQuery).list());
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Optional<Book> getById(Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            Book entity = session.get(Book.class, id);

            return Optional.ofNullable(entity);
        }
    }

    // delete
    public void delete(Long id) {
        Optional<Book> optionalEntity = getById(id);

        if (optionalEntity.isPresent()) {
            delete(optionalEntity.get());
        } else {
            System.err.println("Nie udało się odnaleźć instancji");
        }
    }

    public void delete(Book book) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.delete(book);

            transaction.commit();
        }
    }


    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            query.select(root).where(criteriaBuilder.greaterThan(root.get("numOfAllCopies"), root.get("numOfBorrowedCopies")));
            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }

    public List<Book> getNotAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            query.select(root).where(criteriaBuilder.lessThanOrEqualTo(root.get("numOfAllCopies"), root.get("numOfBorrowedCopies")));
            books.addAll(session.createQuery(query).getResultList());
        }
        return books;

    }

    public List<Book> getBooksBorrowedByClient(Long id) {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> rootBookLent = query.from(BookLent.class);
            Join<BookLent, Book> bookJoinBooklent = rootBookLent.join("book");

            query.select(bookJoinBooklent).where(criteriaBuilder.equal(rootBookLent.get("client"), id));

            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }

    public List<Book> getBooksNotReturnedByClient(Long id) {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> rootBookLent = query.from(BookLent.class);
            Join<BookLent, Book> bookJoinBooklent = rootBookLent.join("book");

            query.select(bookJoinBooklent).where(criteriaBuilder.equal(rootBookLent.get("client"), id), (criteriaBuilder.isNull(rootBookLent.get("dateReturned"))));

            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }

    public List<Book> getBooksByPublishingHouse(Long id) {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = query.from(Book.class);

            query.select(root).where(criteriaBuilder.equal(root.get("publishingHouse"), id));

            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }


    public List<Book> getNotReturnedBooks() {
        List<Book> books = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> rootBookLent = query.from(BookLent.class);
            Join<BookLent, Book> joinBook = rootBookLent.join("book");

            query.select(joinBook).where(criteriaBuilder.isNull(rootBookLent.get("dateReturned")));

            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }

    public List<Book> getBooksReturnedInLastDays(int days) {
        LocalDate date = LocalDate.now().minusDays(days);
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> root = query.from(BookLent.class);
            Join<BookLent, Book> bookRoot = root.join("book");

            query.select(bookRoot).where(criteriaBuilder.greaterThanOrEqualTo(root.get("dateReturned"), date));

            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }

    public List<Book> getBooksBorrowedDuringLastDay() {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
            Root<BookLent> root = query.from(BookLent.class);
            Join<BookLent, Book> bookRoot = root.join("book");
            query.select(bookRoot).where(criteriaBuilder.greaterThanOrEqualTo(root.get("dateLent"), LocalDate.now().minusDays(1)));
            books.addAll(session.createQuery(query).getResultList());
        }
        return books;
    }

    public List<Book> getMostPopularBooks() {
        List<Book> books = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> queryBook = criteriaBuilder.createQuery(Book.class);

            Root<BookLent> rootBookLent = queryBook.from(BookLent.class);
            Join<BookLent, Book> bookLentJoinBook = rootBookLent.join("book");

            queryBook.select(bookLentJoinBook);
            queryBook.groupBy(rootBookLent.get("book"));
            queryBook.orderBy(criteriaBuilder.desc(criteriaBuilder.count(rootBookLent.get("book"))));

            books.addAll(session.createQuery(queryBook).getResultList());
        }
        return books;
    }
}//class
