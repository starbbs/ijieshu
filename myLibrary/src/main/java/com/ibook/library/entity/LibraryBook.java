package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "library_book")
public class LibraryBook implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7378420595475013671L;
    /** 主键id **/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /** 图书馆id **/
    @Column(name = "library_id")
    private int libraryId;
    
    /** 图书拥有者id **/
    @Column(name = "owner_user_id")  
    private int ownerUserId;

    /** 图书id **/
    @Column(name = "book_id")  
    private int bookId;

    /** 书名 **/
    @Column(name = "title") 
    private String title;
    
    /** 封面**/
    @Column(name = "img") 
    private String img;
    
    /** 豆瓣url **/
    @Column(name = "url")     
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
