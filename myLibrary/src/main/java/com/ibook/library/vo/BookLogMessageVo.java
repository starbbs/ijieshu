package com.ibook.library.vo;

import java.util.List;

public class BookLogMessageVo {

    private int logId;
    
    private int bookId;
    
    private int msgId;
    
    private String title;
        
    private String borrowNick;
    
    private int borrowUserId;
    
    private String borrowPassport;
    
    private int borrowReliableNum;
    
    private int borrowUnReliableNum;
    
    private String ownerNick;
    
    private int ownerUserId;
    
    private String ownerPassport;
    
    private int ownerReliableNum;
    
    private int ownerUnReliableNum;

    private String msg;
    
    private String bookImg;
    
    private boolean myBook;
    
    private int status;
    
    private String statusStr;
    
    private List<UserMessageVo> messages;

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

    public String getBorrowNick() {
        return borrowNick;
    }

    public void setBorrowNick(String borrowNick) {
        this.borrowNick = borrowNick;
    }

    public int getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(int borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    public String getBorrowPassport() {
        return borrowPassport;
    }

    public void setBorrowPassport(String borrowPassport) {
        this.borrowPassport = borrowPassport;
    }

    public String getOwnerNick() {
        return ownerNick;
    }

    public void setOwnerNick(String ownerNick) {
        this.ownerNick = ownerNick;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getOwnerPassport() {
        return ownerPassport;
    }

    public void setOwnerPassport(String ownerPassport) {
        this.ownerPassport = ownerPassport;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<UserMessageVo> getMessages() {
        return messages;
    }

    public void setMessages(List<UserMessageVo> messages) {
        this.messages = messages;
    }

    public int getBorrowReliableNum() {
        return borrowReliableNum;
    }

    public void setBorrowReliableNum(int borrowReliableNum) {
        this.borrowReliableNum = borrowReliableNum;
    }

    public int getBorrowUnReliableNum() {
        return borrowUnReliableNum;
    }

    public void setBorrowUnReliableNum(int borrowUnReliableNum) {
        this.borrowUnReliableNum = borrowUnReliableNum;
    }

    public int getOwnerReliableNum() {
        return ownerReliableNum;
    }

    public void setOwnerReliableNum(int ownerReliableNum) {
        this.ownerReliableNum = ownerReliableNum;
    }

    public int getOwnerUnReliableNum() {
        return ownerUnReliableNum;
    }

    public void setOwnerUnReliableNum(int ownerUnReliableNum) {
        this.ownerUnReliableNum = ownerUnReliableNum;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
    
    
}
