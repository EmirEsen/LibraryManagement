package org.example.manager;

import org.example.data.Customer;

interface ICustomerActivities {
    boolean rentBook(Customer customer);
    boolean returnBook(Customer customer);
    void showRentedBooks(Customer customer);

}
