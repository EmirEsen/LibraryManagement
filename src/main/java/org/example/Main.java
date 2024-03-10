package org.example;

import org.example.data.Book;
import org.example.data.Customer;
import org.example.data.HistoryBook;
import org.example.data.ScienceFictionBook;
import org.example.manager.Library;
import org.example.utilities.Util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static Library library = new Library();

    public static void main(String[] args) {

        //initiliazing sample data for testing
        //cutomers
        Customer c1 = new Customer("1234", "ARDA", "Z");
        Customer c2 = new Customer("4321", "BERK", "A");
        Customer c3 = new Customer("4567", "CEM", "B");
        c3.setDateJoined(LocalDate.of(2022, 12, 3));
        c2.setDateJoined(LocalDate.of(1996, 1, 15));
        c1.setDateJoined(LocalDate.of(2000, 1, 15));
        library.getCustomers().put(c1.getIdentityNo(), c1);
        library.getCustomers().put(c2.getIdentityNo(), c2);
        library.getCustomers().put(c3.getIdentityNo(), c3);

        //List of books with same ISBN
        List<Book> books1 = new ArrayList<>();
        books1.add(new ScienceFictionBook("123456", "1984", "George Orwell", "Penguin"));
        books1.add(new ScienceFictionBook("123456", "1984", "George Orwell", "Penguin"));
        List<Book> books2 = new ArrayList<>();
        books2.add(new ScienceFictionBook("98765", "Brave New World", "Aldous Huxley", "Penguin"));
        books2.add(new ScienceFictionBook("98765", "Brave New World", "Aldous Huxley", "Penguin"));
        List<Book> books3 = new ArrayList<>();
        books3.add(new HistoryBook("246246", "How Nations Fail", "Daren Acemoglu", "Penguin"));
        List<Book> books4 = new ArrayList<>();
        books4.add(new HistoryBook("366", "Empire", "Niall Ferguson", "Penguin"));

        //adding books to library
        library.getBooks().put(books1.getFirst().getISBN(), books1);
        library.getBooks().put(books2.getFirst().getISBN(), books2);
        library.getBooks().put(books3.getFirst().getISBN(), books3);
        library.getBooks().put(books4.getFirst().getISBN(), books4);

        System.out.println(books1.getFirst().getISBN());
        System.out.println(books3.getFirst().getISBN());


        systemMenu();


    }

    private static void systemMenu() {
        while (true) {
            System.out.println("""
                    ===== SYSTEM MENU =====
                     1- Rent Operations
                     2- Inventory Menu
                     3- Admin Menu
                     0- Quit.""");
            int input = Util.intScanner("input: ");

            switch (input) {
                case 1 -> rentMenu();
                case 2 -> inventoryMenu();
                case 3 -> adminMenu();
                case 0 -> System.exit(0);
            }
        }
    }

    private static void adminMenu() {

        while (true) {
            System.out.println("""
                    ===== ADMIN MENU =====
                     1- Add New Customer
                     2- Delete Customer
                     3- Show Customer List
                     0- Back.""");
            int input = Util.intScanner("input: ");

            switch (input) {
                case 1 -> library.addNewCustomer();
                case 2 -> library.deleteCustomer();
                case 3 -> library.showCustomerList();
                case 0 -> systemMenu();
            }
        }

    }


    private static void inventoryMenu() {

        while (true) {
            System.out.println("""
                    ===== INVENTORY MENU =====
                     1- Add New Book
                     2- Delete Book
                     3- Show Books [filter]
                     4- Show Available Books
                     5- Show Books at Rent
                     0- Back.""");
            int input = Util.intScanner("input: ");

            switch (input) {
                case 1 -> library.addNewBook();
                case 2 -> library.deleteBook();
                case 3 -> library.showBookInventory();
                case 4 -> library.showAvailableBooks();
                case 5 -> library.showBooksAtRent();
                case 0 -> systemMenu();
            }
        }

    }

    private static void rentMenu() {

        String identityNo = Util.stringScanner("Enter Customer #TC: ").trim();
        Customer customer = library.getCustomers().get(identityNo);

        while (true) {
            System.out.printf("===== TC: %s  Name: %s %s =====\n", customer.getIdentityNo(), customer.getFirstName(), customer.getLastName());
            System.out.println("""
                    1- Rent Book
                    2- Return Book
                    3- Show Rented Books
                    0- Back.""");

            int input = Util.intScanner("input: ");

            switch (input) {
                case 1 -> library.rentBook(customer);
                case 2 -> library.returnBook(customer);
                case 3 -> library.showRentedBooks(customer);
                case 0 -> systemMenu();
            }
        }

    }


}