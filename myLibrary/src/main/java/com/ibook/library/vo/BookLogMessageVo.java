package com.ibook.library.vo;

public class BookLogMessageVo {

    private int logId;
    
    private int bookId;
    
    private int msgId;
    
    private String title;
    
    private String msg;
    
    private String bookImg;
    
    private boolean myBook;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public boolean isMyBook() {
        return myBook;
    }

    public void setMyBook(boolean myBook) {
        this.myBook = myBook;
    }
    
    
}
