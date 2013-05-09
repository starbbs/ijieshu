package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户借书记录
 * @author xiaojianyu
 *
 */
@Entity
@Table(name = "user_book_log")
public class UserBookLog implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 344213697821881710L;
    /**自增主键**/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /** 书id **/
    @Column(name = "book_id")
    private int bookId;
    
    /** 拥有者 **/
    @Column(name = "owner_user_id")
    private int ownerUserId;
    
    /** 拥有者说说 **/
    @Column(name = "owner_talk")
    private String ownerTalk;
    
    /** 拥有者是否靠谱-借阅者评价 **/
    @Column(name = "owner_reliable")
    private int owneReliable;
    /** 借阅者 **/
    @Column(name = "borrow_user_id")
    private int borrowUserId;
    
    /** 借阅者说说 **/
    @Column(name = "borrow_talk")
    private String borrowTalk;
    
    /** 借阅者是否靠谱-拥有者评价 **/
    @Column(name = "borrow_reliable")
    private int borrowReliable;
    
    /**0-申请中，1-已借阅，2-已归还，3-已赠送**/
    @Column(name = "status")
    private int status;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOwnerUserId() {
        return ownerUserId;
    }
    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
    public String getOwnerTalk() {
        return ownerTalk;
    }
    public void setOwnerTalk(String ownerTalk) {
        this.ownerTalk = ownerTalk;
    }
    public int getOwneReliable() {
        return owneReliable;
    }
    public void setOwneReliable(int owneReliable) {
        this.owneReliable = owneReliable;
    }
    public int getBorrowUserId() {
        return borrowUserId;
    }
    public void setBorrowUserId(int borrowUserId) {
        this.borrowUserId = borrowUserId;
    }
    public String getBorrowTalk() {
        return borrowTalk;
    }
    public void setBorrowTalk(String borrowTalk) {
        this.borrowTalk = borrowTalk;
    }
    public int getBorrowReliable() {
        return borrowReliable;
    }
    public void setBorrowReliable(int borrowReliable) {
        this.borrowReliable = borrowReliable;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    } 
    
}
