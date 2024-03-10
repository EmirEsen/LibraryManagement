package org.example.data;

import org.example.utilities.Util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private final String UUid;
    private String identityNo; //tc kimlik no
    private String firstName;
    private String lastName;
    private LocalDate dateJoined;
    private Map<Book, LocalDate[]> rentedBooks; //Hashmap preferred to restrict customer to rent same book twice.


    public Customer(String tc, String firstName, String lastName) {
        this.UUid = Util.UUIDGenerator();
        this.identityNo = tc;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rentedBooks = new HashMap<>();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "UUid='" + UUid + '\'' +
                ", identityNo='" + identityNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateJoined=" + dateJoined +
                ", rentBooks=" + rentedBooks +
                '}';
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getUUid() {
        return UUid;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public Map<Book, LocalDate[]> getRentedBooks() {
        return rentedBooks;
    }
}
