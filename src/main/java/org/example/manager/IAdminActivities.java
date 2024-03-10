package org.example.manager;

import org.example.data.Book;
import org.example.data.Customer;

public interface IAdminActivities {
    Customer addNewCustomer();
    boolean deleteCustomer();
    boolean addNewBook();
    boolean deleteBook();
}
