package com.shosen.max.bean;

public class PayRequest {

    private String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public PayRequest(String bookId) {
        this.bookId = bookId;
    }
}
