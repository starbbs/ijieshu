package com.ibook.library.service;

import java.util.List;

import com.ibook.library.entity.Book;

public interface ThirdService {

    /**
     * 从豆瓣获取一本书
     * @param identity-豆瓣id、ISBN
     * @return
     */
    public Book getBookFromDouban(String identity);
    
    /**
     * 根据查询条件从豆瓣查询书
     * @param query-书名/作者/ISBN
     * @return
     */
    public List<Book> getBooksFromDouban(String query);
}
