import java.time.LocalDate;
import java.util.*;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession().close();
        AuthorDao authorDao = new AuthorDao();
        BookDao bookDao = new BookDao();
        ClientDao clientDao = new ClientDao();
        PublishingHouseDao publishingHouseDao = new PublishingHouseDao();
        BookLentDao bookLentDao = new BookLentDao();



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
                authorDao.delete(id);

            } else if (option == 4){   //    4. -> zmień dane autora
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                Author author = authorDao.getById(id).get();
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
                bookDao.delete(id);

            } else if (option == 8){   //    8. -> zmień dane książki
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                Book book = bookDao.getById(id).get();
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
                clientDao.delete(id);

            } else if (option == 12){//    12. -> zmień dane klienta
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                Client client = clientDao.getById(id).get();
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
                publishingHouseDao.delete(id);

            } else if (option == 16){//    16. -> zmień dane wydawnictwa
                System.out.println("Wpisz id");
                Long id = getLongFromUser();
                PublishingHouse publishingHouse = publishingHouseDao.getById(id).get();
                publishingHouse = modifyPublishingHouse(publishingHouse);
                publishingHouseDao.saveOrUpdate(publishingHouse);

            } else if (option == 17){//    17. -> dodaj książkę do autora
                System.out.println("Podaj id autora:");
                authorDao.getAll().forEach(System.out::println);
                Long idAuthor = getLongFromUser();
                System.out.println("Podaj id książki");
                bookDao.getAll().forEach(System.out::println);
                Long idBook = getLongFromUser();
                Author author = authorDao.getById(idAuthor).get();
                author.addBook(bookDao.getById(idBook).get());
                authorDao.saveOrUpdate(author);

            } else if (option == 18){//    18. -> dodaj wypożyczenie (klient + książka)
                System.out.println("Wpisz id kienta");
                clientDao.getAll().forEach(System.out::println);
                Long idClient = getLongFromUser();
                Client client = clientDao.getById(idClient).get();
                System.out.println("Wpisz id książki");
                bookDao.getAll().forEach(System.out::println);
                Long idBook = getLongFromUser();
                Book book = bookDao.getById(idBook).get();
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
                authors.forEach(System.out::println);

            } else if (option == 20){//    20. -> znajdź klientów po nazwisku
                System.out.println("Wpisz nazwisko lub fragment nazwiska");
                String name = scanner.nextLine();
                List<Client> clients = clientDao.getByName(name);
                clients.forEach(System.out::println);

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
                books.forEach(System.out::println);

            } else if (option == 23){//    23. -> wypisz książki nie zwrócone przez klienta
                System.out.println("Wpisz id klienta");
                Long id = getLongFromUser();
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksNotReturnedByClient(id));
                books.forEach(System.out::println);

            } else if (option == 24){//    24. -> wypisz książki których są jeszcze kopie do pożyczenia
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getAvailableBooks());
                books.forEach(System.out::println);

            } else if (option == 25){//    25. -> wypisz książki których nie ma juz kopii do pożyczenia
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getNotAvailableBooks());
                books.forEach(System.out::println);

            } else if (option == 26){//    26. -> wypisz książki które nie zostały zwrócone
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getNotReturnedBooks());
                books.forEach(System.out::println);

            } else if (option == 27){//    27. -> wypisz ksiązki które zostały zwrócone w ciągu ostatnich N godzin
                System.out.println("Wpisz liczbę dni");
                int days = getIntFromUser();
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksReturnedInLastDays(days));
                books.forEach(System.out::println);

            } else if (option == 28){//    28. -> wypisz książki które zostały wypożyczone w ciągu ostatnich 24 godzin
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getBooksBorrowedDuringLastDay());
                books.forEach(System.out::println);

            } else if (option == 29){//    29. -> wypisz najczęściej wypożyczone książki
                List<Book> books = new ArrayList<>();
                books.addAll(bookDao.getMostPopularBooks());
                books.forEach(System.out::println);

            } else if (option == 30){//    30. -> znajdź najbardziej aktywnego klienta
                List<Client> clients = new ArrayList<>();
                clients.addAll(clientDao.getMostActiveClients());
                clients.forEach(System.out::println);

            } else if (option == 31){//    31. -> dodaj wydawnictwo do książki
                bookDao.getAll().forEach(System.out::println);
                System.out.println("Wpisz id książki");
                Long bookId = getLongFromUser();
                Book book = bookDao.getById(bookId).get();
                publishingHouseDao.getAll().forEach(System.out::println);
                System.out.println("wpisz id wydawnictwa");
                Long publishingHouseId = getLongFromUser();
                PublishingHouse publishingHouse = publishingHouseDao.getById(publishingHouseId).get();
                book.setPublishingHouse(publishingHouse);
                System.out.println("dodano wydawnictwo: " + book);
                System.out.println("zaktualizowano: " + bookDao.saveOrUpdate(book));

            } else if (option == 32){//    32. -> wypisz książki po wydawnictwie
                List<Book> books = new ArrayList<>();
                System.out.println("wpisz id wydawnictwa");
                Long id = getLongFromUser();
                books.addAll(bookDao.getBooksByPublishingHouse(id));
                books.forEach(System.out::println);

            } else if (option == 33) {//   33. -> oddaj książkę
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
        System.out.println("Wpisz rok");
        int year = getIntFromUser();
        System.out.println("Wpisz miesiąc");
        int month = getIntFromUser();
        System.out.println("Wpisz dzień");
        int day = getIntFromUser();
        LocalDate date = LocalDate.of(year, month, day);
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
        System.out.println("29. -> wypisz najczęściej wypożyczone książki");
        System.out.println("30. -> znajdź najbardziej aktywnego klienta");
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
                nfe.printStackTrace();
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
                nfe.printStackTrace();
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
                nfe.printStackTrace();
            }
        } while (value == null);
        return value;
    }


}//class
