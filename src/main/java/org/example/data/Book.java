package org.example.data;

import org.example.manager.Library;
import org.example.utilities.Util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Book {
    public enum Status {
        AVAILABLE, ON_RENT, NOT_AVAILABLE
    }
    private final String UUID;
    private String ISBN;
    private String name;
    private String author;
    private String publisher;
    private Status status;

    // ISBN will be entered by user, however in real world it can be scanned from book cover
    public Book(String ISBN, String name, String author, String publisher) {
        this.UUID = Util.UUIDGenerator();
        this.ISBN = ISBN;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.status = Status.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Book{" +
                "UUID='" + UUID + '\'' +
                ", ISBN='" + ISBN + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", status=" + status +
                '}';
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUUID() {
        return UUID;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }


    public Status getStatus() {
        return status;
    }
}
