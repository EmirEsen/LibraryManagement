package org.example.manager;

import org.example.data.*;
import org.example.utilities.Util;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class Library implements ILibraryActivities, ICustomerActivities, IAdminActivities {
    private Map<String, List<Book>> books = new LinkedHashMap<>(); //(k-isbn, List of books with same ISBN)
    private Map<String, Customer> customers = new LinkedHashMap<>();

    @Override
    public boolean rentBook(Customer customer) {
        //        String isbn = Util.UUIDGenerator(); //in real life ISBN will be scanned from back cover of the book.
        String isbn = Util.stringScanner("ISBN: "); //for testing
        String duration = Util.stringScanner("Rent Duration (Days): ");
        List<Book> isbnSameBooks = books.get(isbn);

        if (customer.getRentedBooks().containsKey(isbnSameBooks.getFirst())) {
            System.out.println("\u001B[31mBook has been already rented, Can not rent the same books.\u001B[0m");
        }

        for (Book book : isbnSameBooks) {
            if (book != null && book.getStatus().equals(Book.Status.AVAILABLE)) { //Book stock won't be revised, I assume customer must find the book physically to rent.
                book.setStatus(Book.Status.ON_RENT);
                LocalDate rentedOn = LocalDate.now();
                LocalDate rentUntil = rentedOn.plusDays(Long.parseLong(duration));
                customer.getRentedBooks().put(book, new LocalDate[]{rentedOn, rentUntil});
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean returnBook(Customer customer) {
        //        String isbn = Util.UUIDGenerator(); //in real life ISBN will be scanned from back cover of the book.
        String isbn = Util.stringScanner("ISBN: "); //for testing
        Map<Book, LocalDate[]> rentedBooks = customer.getRentedBooks();

        for (Map.Entry<Book, LocalDate[]> entry : rentedBooks.entrySet()) {
            Book book = entry.getKey();
            LocalDate[] rentalDates = entry.getValue();
            Period rentalPeriod = Period.between(rentalDates[0], rentalDates[1]);
            LocalDate currentDate = LocalDate.now();

            if (currentDate.isAfter(rentalDates[1])) {
                System.out.println("Customer has exceeded the rental dates by " + rentalPeriod.getDays());
                Util.stringScanner("");// pause the program to make any further operations about additional fee for exceeding rental return time
            }

            if (book.getISBN().equals(isbn)) {
                book.setStatus(Book.Status.AVAILABLE);
                rentedBooks.remove(book);

                rentalDates[0] = null;
                rentalDates[1] = null;

                return true;
            }
        }
        return false;
    }

    @Override
    public void showRentedBooks(Customer customer) {
        Map<Book, LocalDate[]> rentedBooks = customer.getRentedBooks();
        String header = "%s %s %s Rented Books".formatted(customer.getIdentityNo(), customer.getFirstName(), customer.getLastName());
        System.out.println("===============" + header + "===============");
        System.out.printf(" | %-20s | %-8s | %4s |%n", "BOOK NAME", "PUBLISHER", "RENT DAYS LEFT");
        for (Map.Entry<Book, LocalDate[]> entry : rentedBooks.entrySet()) {
            Book book = entry.getKey();
            LocalDate[] rentalDates = entry.getValue();
            LocalDate currentDate = LocalDate.now();
            long daysUntilReturn = ChronoUnit.DAYS.between(currentDate, rentalDates[1]);

            if (currentDate.isAfter(rentalDates[1])) { //if the return date passed, print the exceeded day number in Red else green
                System.out.printf(">| %-20S | %-8S | \u001B[31m%4s\u001B[0m |%n", book.getName(), book.getPublisher(), daysUntilReturn);
            } else {
                System.out.printf(">| %-20S | %-8S | \u001B[32m%4s\u001B[0m |%n", book.getName(), book.getPublisher(), daysUntilReturn);
            }
        }
        System.out.println("-----------------------------------------------");
    }

    @Override
    public void showBookInventory() {
        System.out.println("""
                ===== SHOW BOOKS BY =====
                 1- Genre
                 2- Author
                 3- Name
                 4- No Filter
                """);
        String input = Util.stringScanner("Input: ").trim();
        switch (Integer.parseInt(input)) {
            case 1 -> {
                System.out.println("-----------------------------------------------");
                String genreInput = Util.stringScanner("Genre: ").trim();
                books.values().stream()
                        .flatMap(List::stream)
                        .filter(book -> book.getClass().getSimpleName().equalsIgnoreCase(genreInput + "book")) //added "book" because subclass names finishes with book but most probably users will not write it.
                        .forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }
            case 2 -> {
                System.out.println("-----------------------------------------------");
                String author = Util.stringScanner("Author: ").trim();
                books.values().stream()
                        .flatMap(List::stream)
                        .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                        .forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }

            case 3 -> {
                System.out.println("-----------------------------------------------");
                String bookName = Util.stringScanner("Book Name: ").trim();
                books.values().stream()
                        .flatMap(List::stream)
                        .filter(book -> book.getName().equalsIgnoreCase(bookName))
                        .forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }
            case 4 -> {
                System.out.println("-----------------------------------------------");
                books.values().stream()
                        .flatMap(List::stream)
                        .sorted(new Comparator<Book>() {
                            @Override
                            public int compare(Book o1, Book o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        })
                        .forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }
        }
    }


    @Override
    public void showAvailableBooks() {
        System.out.println("-----------------------------------------------");
        books.values().stream()
                .flatMap(List::stream)
                .filter(book -> book.getStatus().equals(Book.Status.AVAILABLE))
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }


    @Override
    public void showBooksAtRent() {
        System.out.println("-----------------------------------------------");
        books.values().stream()
                .flatMap(List::stream)
                .filter(book -> book.getStatus().equals(Book.Status.ON_RENT))
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }

    @Override
    public void showCustomerList() {
        System.out.println("""
                -----SHOW CUSTOMERS-----
                 1- List By First Name
                 2- List By Last Name
                 3- Date Joined After Year
                """);
        String input = Util.stringScanner("Input: ").trim();
        switch (Integer.parseInt(input)) {
            case 1 -> {
                System.out.println("-----------------------------------------------");
                customers.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue().getFirstName())).map(Map.Entry::getValue).forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }
            case 2 -> {
                System.out.println("-----------------------------------------------");
                customers.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue().getLastName())).map(Map.Entry::getValue).forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }
            case 3 -> {
                int year = Util.intScanner("Enter Year: ");
                System.out.println("-----------------------------------------------");
                customers.values().stream().filter(customer -> customer.getDateJoined().getYear() > year).forEach(System.out::println);
                System.out.println("-----------------------------------------------");
            }
        }
    }

    @Override
    public Customer addNewCustomer() {
        String identityNo = Util.stringScanner("Enter Customer #TC: ").trim();
        if (!customers.containsKey(identityNo)) {
            String firstName = Util.stringScanner("Enter Customer First_Name: ");
            String lastName = Util.stringScanner("Enter Customer Last_Name: ");
            Customer customer = new Customer(identityNo, firstName.toUpperCase(), lastName.toUpperCase());
            customer.setDateJoined(LocalDate.now());
            return customers.put(identityNo, customer);
        }
        return customers.get(identityNo);
    }


    @Override
    public boolean deleteCustomer() {
        String identityNo = Util.stringScanner("Enter Customer #TC: ").trim();
        if (customers.containsKey(identityNo)) {
            customers.remove(identityNo);
            return true;
        } else {
            System.out.println(identityNo + "can not found.");
        }
        return false;
    }


    @Override
    public boolean addNewBook() {
//        String isbn = Util.UUIDGenerator(); //in real life ISBN will be scanned from book back cover
        String isbn = Util.stringScanner("ISBN: "); //for testing

        if (books.containsKey(isbn)) {
            List<Book> booksWithSameISBN = books.get(isbn);
            Book book = this.createNewBookInstanceAccordingToGenre(isbn);
            return booksWithSameISBN.add(book);
        }

        String name = Util.stringScanner("Book Name: ").toUpperCase().trim();
        String author = Util.stringScanner("Book Author: ").toUpperCase().trim();
        String publisher = Util.stringScanner("Book publisher: ").toUpperCase().trim();
        String genre = Util.stringScanner("Book Genre: ").toUpperCase().trim();
        Book book = null;
        switch (genre) {
            case "SCIENCE FICTION", "SCIENCE-FICTION", "SCI-FI":
                book = new ScienceFictionBook(isbn, name, author, publisher);
                break;
            case "HISTORY":
                book = new HistoryBook(isbn, name, author, publisher);
                break;
            case "FICTION", "NOVEL":
                book = new FictionBook(isbn, name, author, publisher);
                break;
            default:
                System.out.println("Unknown genre: " + genre);

        }
        if (book != null) {
            List<Book> booksWithSameISBN = new LinkedList<>();
            booksWithSameISBN.add(book);
            books.put(book.getISBN(), booksWithSameISBN);
            System.out.printf("ISBN: %s NAME: %s, PUBLISHER: %s, added to the inventory first time.\n", book.getISBN(), book.getName(), book.getPublisher());
            return true;
        }
        return false;
    }

    //Helper method to create Book instance according to its class/Genre
    private Book createNewBookInstanceAccordingToGenre(String isbn) {
        Book book = books.get(isbn).getFirst();
        switch (book.getClass().getSimpleName()) {
            case "ScienceFictionBook":
                book = new ScienceFictionBook(isbn, book.getName(), book.getAuthor(), book.getPublisher());
                break;
            case "HistoryBook":
                book = new HistoryBook(isbn, book.getName(), book.getAuthor(), book.getPublisher());
                break;
            case "FictionBook":
                book = new FictionBook(isbn, book.getName(), book.getAuthor(), book.getPublisher());
                break;
        }
        return book;
    }

    @Override
    public boolean deleteBook() {
//    String isbn = Util.UUIDGenerator(); //in real life ISBN will be scanned from book back cover
        String isbn = Util.stringScanner("ISBN: ").trim(); //for testing
        List<Book> booksWithSameISBN = books.get(isbn);

        if (booksWithSameISBN != null) {
            printsBooksWithSameISBN(booksWithSameISBN);
            String uuid = Util.stringScanner("Enter Book UUID to remove: ");
            for (Book b : booksWithSameISBN) {
                if (b.getUUID().equals(uuid)) {
                    return booksWithSameISBN.remove(b);
                }
            }
            System.out.println(uuid + " can not found.");
            return false;
        }
        System.out.println("Book with " + isbn + " can not found.");
        return false;
    }

    //Prints Books with same ISBN to console
    private void printsBooksWithSameISBN(List<Book> booksWithSameISBN) {
        System.out.println("-----------------------------------------------");
        booksWithSameISBN.forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }


    //For initializing sample data in org.example.Main()
    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<String, List<Book>> getBooks() {
        return books;
    }
}
