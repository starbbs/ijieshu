package com.ibook.library.service;

import java.util.List;

import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.vo.BookLogMessageVo;
import com.ibook.library.vo.BookVo;
import com.ibook.library.vo.UserLibraryVo;

public interface LibraryService {

    public UserLibrary saveUserLibrary(Library library, int userId);

    public List<UserLibraryVo> getUserLibraryList(int userId);

    public Book saveBook(int userId, String identity);

    public List<Book> getBookList(int userId);

    public List<BookVo> getLibraryBookList(int userId, String query);

    public List<BookVo> getBookList(String query);

    public boolean saveBorrowBookRequest(int userId, int bookId, String msg);

    public boolean sendMessage(int userId, int oldMsgId, String msg);

    public boolean revertBook(int userId, int logId);

    public List<Library> getLibrarys(String query);

    public List<BookLogMessageVo> getBookLogMessageVos(int userId);

    public boolean quitLibrary(int userLibraryId, int libraryId, int userId);

    public boolean updateUserInfo(UserInfo userInfo);

    public boolean findPassword(String passport);

    public int getUserCount();

    public int getLibraryCount();
    
    public int getBookCount();
}
