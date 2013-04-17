package com.ibook.library.vo;

import java.util.Set;

import com.ibook.library.entity.Book;

public class BookVo extends Book{

    /**
     * 
     */
    private static final long serialVersionUID = -6653693364679488249L;

    private Set<Integer> libraryId;

    public Set<Integer> getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Set<Integer> libraryId) {
        this.libraryId = libraryId;
    }
 
}

