package com.ibook.library.vo;

public class UserLibraryVo {

    private int id;
    
    private int userId;
    
    private int libraryId;
    
    private String libraryName;
    
    private String libraryDesc;

    private int bookCount;
    
    private int userCount;

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

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
    
    
}
