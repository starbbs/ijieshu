package com.ibook.library.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 用户的地理标签
 * @author xiaojianyu
 *
 */
@Entity
@Table(name = "user_library")
public class UserLibrary implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5450699340766885857L;
    
    /** 主键id **/
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /** 用户id **/
    @Column(name = "user_id")
    private int userId;

    /** libarayId图书馆id **/
    @Column(name = "library_id")
    private int libraryId;
    
    /** libraryName图书馆名字 **/
    @Column(name = "library_name")
    private String libraryName;
    
    /** libraryDesc图书馆描述 **/
    @Column(name = "library_desc")
    private String libraryDesc;
    
    /** 在该图书馆的昵称 **/
    @Column(name = "nick")    
    private String nick;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryDesc() {
        return libraryDesc;
    }

    public void setLibraryDesc(String libraryDesc) {
        this.libraryDesc = libraryDesc;
    }

    
}
