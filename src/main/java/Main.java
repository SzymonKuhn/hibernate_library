import java.time.LocalDate;
import java.util.*;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private final static ClientDao clientDao = new ClientDao();
    private final static AuthorDao authorDao = new AuthorDao();
    private final static BookDao bookDao = new BookDao();

    private final static PublishingHouseDao publishingHouseDao = new PublishingHouseDao();
    private final static BookLentDao bookLentDao = new BookLentDao();

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession().close();




        int option;
        do {
            printOptions();
            option = getIntFromUser();

            if (option==1) {     //    1. -> dodaj autora
                Author author = getAuthorFromUser();
                System.out.println("Dodano obiekt? " + authorDao.saveOrUpdate(author));

            } else if (option == 2){   //    2. -> wypisz autorów
                authorDao.getAll().forEach(System.out::println);

            } else if (option == 3){   //    3. -> usuń autora po id
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                System.out.println(authorDao.delete(id));

            } else if (option == 4){   //    4. -> zmień dane autora
                Author author = getAuthorFromDB();
                author = modifyAuthor(author);
                authorDao.saveOrUpdate(author);

            } else if (option == 5){   //    5. -> dodaj książkę
                Book book = getBookFromUser();
                System.out.println("Dodano obiekt? " + bookDao.saveOrUpdate(book));

            } else if (option == 6){   //    6. -> wypisz książki
                bookDao.getAll().forEach(System.out::println);

            } else if (option == 7){   //    7. -> usuń książkę po id
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                System.out.println(bookDao.delete(id));

            } else if (option == 8){   //    8. -> zmień dane książki
                Book book = getBookFromDB();
                book = modifyBook(book);
                bookDao.saveOrUpdate(book);

            } else if (option == 9){   //    9. -> dodaj klienta
                Client client = getClientFromUser();
                System.out.println("Dodano obiekt? " + clientDao.saveOrUpdate(client));

            } else if (option == 10){//    10. -> wypisz klientów
                clientDao.getAll().forEach(System.out::println);

            } else if (option == 11){//    11. -> usuń klienta po id
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                System.out.println(clientDao.delete(id));

            } else if (option == 12){//    12. -> zmień dane klienta
                Client client = getClientFromDB();
                client = modifyClient(client);
                clientDao.saveOrUpdate(client);

            } else if (option == 13){//    13. -> dodaj wydawnictwo
                PublishingHouse publishingHouse = getPublishingHouseFromUser();
                System.out.println("Dodano obiekt? " + publishingHouseDao.saveOrUpdate(publishingHouse));

            } else if (option == 14) {//    14 -> wypisz wydawnictwa
                publishingHouseDao.getAll().forEach(System.out::println);

            } else if (option == 15) {//    15. -> usuń wydawnictwo
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                System.out.println(publishingHouseDao.delete(id));

            } else if (option == 16){//    16. -> zmień dane wydawnictwa
                PublishingHouse publishingHouse = getPublishingHouseFromDB();
                publishingHouse = modifyPublishingHouse(publishingHouse);
                publishingHouseDao.saveOrUpdate(publishingHouse);

            } else if (option == 17){//    17. -> dodaj książkę do autora
                authorDao.getAll().forEach(System.out::println);
                Author author = getAuthorFromDB();

                bookDao.getAll().forEach(System.out::println);
                Book book = getBookFromDB();

                author.addBook(book);
                authorDao.saveOrUpdate(author);

            } else if (option == 18){//    18. -> dodaj wypożyczenie (klient + książka)
                clientDao.getAll().forEach(System.out::println);
                Client client = getClientFromDB();

                bookDao.getAll().forEach(System.out::println);
                Book book = getBookFromDB();

                if (book.getNumOfAllCopies() > book.getNumOfBorrowedCopies()) {
                    LocalDate localDate = LocalDate.now();
                    BookLent bookLent = new BookLent();
                    bookLent.setClient(client);
                    bookLent.setBook(book);
                    bookLent.setDateLent(localDate);
                    System.out.println("Dodano obiekt? " + bookLentDao.saveOrUpdate(bookLent));
                } else {
                    System.out.println("liczba kopii książki: " + book.getNumOfAllCopies());
                    System.out.println("liczba wypożyczonych kopii: " + book.getNumOfBorrowedCopies());
                    System.out.println("Nie można pożyczyć książki, wszystkie są wypożyczone");
                }

            } else if (option == 19){//    19. -> znajdź autorów po nazwisku
                System.out.println("Wpisz nazwisko lub fragment nazwiska");
                String name = scanner.nextLine();
                List<Author> authors = authorDao.getByName(name);
                printListIfNotEmpty(authors);

            } else if (option == 20){//    20. -> znajdź klientów po nazwisku
                System.out.println("Wpisz nazwisko lub fragment nazwiska");
                String name = scanner.nextLine();
                List<Client> clients = clientDao.getByName(name);
                printListIfNotEmpty(clients);

            } else if (option == 21){//    21. -> znajdź klienta po Id number
                Optional<Client> clientOptional;
                do {
                    System.out.println("Wpisz numer dowodu tożsamości");
                    String idNumber = scanner.nextLine();
                    clientOptional = clientDao.getByIdNumber(idNumber);
                    if (!clientOptional.isPresent()) {
                        System.out.println("nie znaleziono instancji");
                    }
                } while (!clientOptional.isPresent());
                System.out.println("Znaleziono klienta: " + clientOptional.get().toString());

            } else if (option == 22){//    22. -> wypisz książki wypożyczone przez klienta
                System.out.println("Wpisz id klienta");
                Long id = getLongFromUser();
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksBorrowedByClient(id));
                printListIfNotEmpty(books);


            } else if (option == 23){//    23. -> wypisz książki nie zwrócone przez klienta
                System.out.println("Wpisz id klienta");
                Long id = getLongFromUser();
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksNotReturnedByClient(id));
                printListIfNotEmpty(books);

            } else if (option == 24){//    24. -> wypisz książki których są jeszcze kopie do pożyczenia
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getAvailableBooks());
                printListIfNotEmpty(books);

            } else if (option == 25){//    25. -> wypisz książki których nie ma juz kopii do pożyczenia
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getNotAvailableBooks());
                printListIfNotEmpty(books);

            } else if (option == 26){//    26. -> wypisz książki które nie zostały zwrócone
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getNotReturnedBooks());
                printListIfNotEmpty(books);

            } else if (option == 27){//    27. -> wypisz ksiązki które zostały zwrócone w ciągu ostatnich N godzin
                System.out.println("Wpisz liczbę dni");
                int days = getIntFromUser();
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksReturnedInLastDays(days));
                printListIfNotEmpty(books);

            } else if (option == 28){//    28. -> wypisz książki które zostały wypożyczone w ciągu ostatnich 24 godzin
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksBorrowedDuringLastDay());
                printListIfNotEmpty(books);

            } else if (option == 29){//    29. -> lista książek od najchętniej wypożyczanych
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getMostPopularBooks());
                printListIfNotEmpty(books);

            } else if (option == 30){//    30. -> lista klientów od najczęściej pożyczających
                List<Client> clients = new ArrayList<>();
                clients.addAll(clientDao.getMostActiveClients());
                printListIfNotEmpty(clients);

            } else if (option == 31){//    31. -> dodaj wydawnictwo do książki
                bookDao.getAll().forEach(System.out::println);
                Book book = getBookFromDB();

                publishingHouseDao.getAll().forEach(System.out::println);
                PublishingHouse publishingHouse = getPublishingHouseFromDB();

                book.setPublishingHouse(publishingHouse);
                System.out.println("dodano wydawnictwo: " + book);
                System.out.println("zaktualizowano: " + bookDao.saveOrUpdate(book));

            } else if (option == 32){//    32. -> wypisz książki po wydawnictwie
                List<Book> books = new ArrayList<>();
                System.out.println("wpisz id wydawnictwa");
                Long id = getLongFromUser();
                books.addAll(bookDao.getBooksByPublishingHouse(id));
                printListIfNotEmpty(books);

            } else if (option == 33) {//   33. -> oddaj książkę //TODO Refactor
                System.out.println("podaj id klienta");
                Long clientId = getLongFromUser();
                List<Book> books = bookDao.getBooksNotReturnedByClient(clientId);
                if (!books.isEmpty()) {
                    System.out.println("Wypożyczone książki do oddania:");
                    books.forEach(System.out::println);
                    System.out.println("wpisz id książki do oddania");
                    Long bookId = getLongFromUser();
                    List<BookLent> booklents = new ArrayList<>();
                    booklents.addAll(bookLentDao.getBookLentNotReturned(clientId, bookId));
                    BookLent booklentToReturn = getBooklentToReturnFromBooklents(booklents);

                    booklentToReturn.setDateReturned(LocalDate.now());
                    System.out.println("Zwrot wypożyczenia: " + booklentToReturn);
                    System.out.println("zwrócono? " + bookLentDao.saveOrUpdate(booklentToReturn));
                } else {
                    System.out.println("Brak pożyczonych książek przez klienta o id="+clientId);
                }
            }
        } while (option!=0);
    }//main

    private static <T> void printListIfNotEmpty(List<T> list) {
        if (!list.isEmpty()) {
            list.forEach(System.out::println);
        } else {
            System.out.println("nie znaleziono instancji");
        }
    }

    private static PublishingHouse getPublishingHouseFromDB() {
        Optional<PublishingHouse> optionalPublishingHouse;
        do {
            System.out.println("Wpisz id");
            Long id = getLongFromUser();
            optionalPublishingHouse = publishingHouseDao.getById(id);
        } while (!optionalPublishingHouse.isPresent());
        return optionalPublishingHouse.get();
    }

    private static Book getBookFromDB() {
        Optional<Book> optionalBook;
        do {
            System.out.println("Wpisz id");
            Long id = getLongFromUser();
            optionalBook = bookDao.getById(id);
        } while (!optionalBook.isPresent());
        return optionalBook.get();
    }

    private static Author getAuthorFromDB() {
        Optional<Author> authorOptional;
        do {
            System.out.println("Wpisz id");
            Long id = getLongFromUser();
            authorOptional = authorDao.getById(id);
        } while (!authorOptional.isPresent());
        return authorOptional.get();
    }

    private static Client getClientFromDB() {
        Optional<Client> optionalClient;
        do {
            System.out.println("Wpisz id");
            Long id = getLongFromUser();
            optionalClient = clientDao.getById(id);
        } while (!optionalClient.isPresent());
        return optionalClient.get();
    }

    private static BookLent getBooklentToReturnFromBooklents(List<BookLent> booklents) {
        BookLent booklentToReturn = null;
        if (booklents.size()>1) {
            booklents.forEach(System.out::println);
            System.out.println("wybierz id wypożyczenia");
            Long id = getLongFromUser();
            for (BookLent booklent : booklents) {
                if (booklent.getId().equals(id)) {
                    booklentToReturn=booklent;
                }
            }
        } else {
            booklentToReturn = booklents.get(0);
        }
        return booklentToReturn;
    }

    private static PublishingHouse modifyPublishingHouse(PublishingHouse publishingHouse) {
        System.out.println("Zmieniasz dane: " + publishingHouse.toString());
        System.out.println("Wpisz nazwę");
        publishingHouse.setName(scanner.nextLine());
        return publishingHouse;
    }

    private static PublishingHouse getPublishingHouseFromUser() {
        PublishingHouse publishingHouse = new PublishingHouse();
        System.out.println("Wpisz nazwę");
        publishingHouse.setName(scanner.nextLine());
        return publishingHouse;
    }

    private static Client modifyClient(Client client) {
        System.out.println("Zmieniasz dane: " + client.toString());
        String option = null;
        System.out.println("Chcesz zmienić imie? (T)");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            client.setName(scanner.nextLine());
        }
        System.out.println("Chcesz zmienić nazwisko?");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            client.setSurname(scanner.nextLine());
        }
        System.out.println("Chcesz zmienić nr dowodu?");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            client.setIdNumber(scanner.nextLine());
        }
        return client;
    }

    private static Client getClientFromUser() {
        Client client = new Client();
        System.out.println("Podaj imie");
        client.setName(scanner.nextLine());
        System.out.println("Podaj nazwisko");
        client.setSurname(scanner.nextLine());
        System.out.println("Podaj numer dowodu tożsamości");
        client.setIdNumber(scanner.nextLine());
        return client;
    }


    private static Book modifyBook(Book book) {
        System.out.println("Zmieniasz dane: " + book.toString());
        String option = null;
        System.out.println("Chcesz zmienić tytuł? (T)");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            book.setTitle(scanner.nextLine());
        }
        System.out.println("Chcesz zmienić rok powstania?");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            book.setYearWritten(getIntFromUser());
        }
        System.out.println("Chcesz zmienić ilość stron?");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            book.setNumOfPages(getIntFromUser());
        }
        System.out.println("Chcesz zmienić ilość kopii");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            book.setNumOfAllCopies(getIntFromUser());
        }
        return book;
    }

    private static Book getBookFromUser() {
        Book book = new Book();
        System.out.println("Wpisz tytuł");
        book.setTitle(scanner.nextLine());
        System.out.println("Wpisz rok powstania");
        book.setYearWritten(getIntFromUser());
        System.out.println("Wpisz ilość stron");
        book.setNumOfPages(getIntFromUser());
        System.out.println("Wpisz ilosć kopii");
        book.setNumOfAllCopies(getIntFromUser());
        return book;
    }

    private static Author getAuthorFromUser() {
        Author author = new Author();
        System.out.println("Wpisz imie");
        author.setName(scanner.nextLine());
        System.out.println("Wpisz nazwisko");
        author.setSurname(scanner.nextLine());
        author.setDateOfBirth(getDateFromUser());
        return author;
    }

    private static Author modifyAuthor(Author author) {
        System.out.println("Zmieniasz dane: " + author.toString());
        String option = null;
        System.out.println("Chcesz zmienić imie? (T)");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            author.setName(scanner.nextLine());
        }
        System.out.println("Chcesz zmienić nazwisko? (T)");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            author.setSurname(scanner.nextLine());
        }
        System.out.println("Chcesz zmienić datę urodzenia? (T)");
        option = scanner.nextLine();
        if (option.equalsIgnoreCase("t")) {
            LocalDate date = getDateFromUser();
            author.setDateOfBirth(date);
        }
        return author;
    }

    private static LocalDate getDateFromUser() {
        LocalDate date = null;
        do {
            System.out.println("Wpisz rok");
            int year = getIntFromUser();
            System.out.println("Wpisz miesiąc");
            int month = getIntFromUser();
            System.out.println("Wpisz dzień");
            int day = getIntFromUser();
            try {
                date = LocalDate.of(year, month, day);
            } catch (Exception e) {
                System.out.println("podano błędne dane");
            }
        } while (date == null);
        return  date;
    }

    private static void printOptions() {
        System.out.println("1. -> dodaj autora");
        System.out.println("2. -> wypisz autorów");
        System.out.println("3. -> usuń autora po id");
        System.out.println("4. -> zmień dane autora");
        System.out.println();
        System.out.println("5. -> dodaj książkę");
        System.out.println("6. -> wypisz książki");
        System.out.println("7. -> usuń książkę po id");
        System.out.println("8. -> zmień dane książki");
        System.out.println("");
        System.out.println("9. -> dodaj klienta");
        System.out.println("10. -> wypisz klientów");
        System.out.println("11. -> usuń klienta po id");
        System.out.println("12. -> zmień dane klienta");
        System.out.println();
        System.out.println("13. -> dodaj wydawnictwo");
        System.out.println("14 -> wypisz wydawnictwa");
        System.out.println("15. -> usuń wydawnictwo");
        System.out.println("16. -> zmień dane wydawnictwa");
        System.out.println("");
        System.out.println("17. -> dodaj książkę do autora");
        System.out.println("18. -> dodaj wypożyczenie (klient + książka)");
        System.out.println("19. -> znajdź autorów po nazwisku");
        System.out.println("20. -> znajdź klientów po nazwisku");
        System.out.println("21. -> znajdź klienta po Id number");
        System.out.println("");
        System.out.println("22. -> wypisz książki wypożyczone przez klienta");
        System.out.println("23. -> wypisz książki nie zwrócone przez klienta");
        System.out.println("24. -> wypisz książki których są jeszcze kopie do pożyczenia");
        System.out.println("25. -> wypisz książki których nie ma juz kopii do pożyczenia");
        System.out.println("26. -> wypisz książki które nie zostały zwrócone");
        System.out.println("27. -> wypisz ksiązki które zostały zwrócone w ciągu ostatnich N godzin");
        System.out.println("28. -> wypisz książki które zostały wypożyczone w ciągu ostatnich 24 godzin");
        System.out.println("29. -> lista książek od najchętniej wypożyczanych");
        System.out.println("30. -> lista klientów od najczęściej pożyczających");
        System.out.println("31. -> dodaj wydawnictwo do książki");
        System.out.println("32. -> wypisz książki po wydawnictwie");
        System.out.println("33. -> oddaj książkę");
        System.out.println("0. -> koniec");
    }

    private static int getIntFromUser() {
        Integer value = null;
        do {
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Błędne dane");
            }
        } while (value == null);
        return value;
    }

    private static long getLongFromUser() {
        Long value = null;
        do {
            String input = scanner.nextLine();
            try {
                value = Long.parseLong(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Błędne dane");
            }
        } while (value == null);
        return value;
    }

    private static double getDoubleFromUser() {
        Double value = null;
        do {
            String input = scanner.nextLine();
            try {
                value = Double.parseDouble(input);
            } catch (NumberFormatException nfe) {
                System.out.println("Błędne dane");
            }
        } while (value == null);
        return value;
    }


}//class
