package com.ibook.library.service;

import java.util.List;

import com.ibook.library.entity.Book;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.LibraryBook;
import com.ibook.library.entity.PresentBookLog;
import com.ibook.library.entity.UserBookLog;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.entity.UserMessage;
import com.ibook.library.util.Page;
import com.ibook.library.vo.BookVo;

public interface CacheService {
    
    public UserInfo getUserInfo(String passport);
    
    public void putUserInfo(UserInfo userInfo);
    
    public UserInfo saveUserInfo(String passport,String password);
      
    public boolean updateUserInfo(UserInfo userInfo);
    
    public void removeUserInfo(String passport);
    
    public Library saveLibrary(Library library);
    
    public Library getLibrary(int id);
    
    public Library getLibrary(String name);
       
    public void putLibrary(Library library);
    
    public void putLibrary(Library library,int id);
    
    public UserLibrary saveUserLibrary(UserLibrary userLibrary);
    
    public void removeUserLibrary(int userId);
    
    public void putUserLibraryList(int userId,List<UserLibrary> list);
    
    public List<UserLibrary> getUserLibraryList(int userId);
    
    public UserLibrary getUserLibrary(int userId,int libraryId);
    
    public void putUserLibrary(UserLibrary userLibrary);
    
    public Book saveBook(Book book);
    
    public boolean updateBook(Book book);
    
    public boolean lockLibraryBook(int bookId);
    
    
    public boolean unLockLibraryBook(int bookId);
    
    public List<Book> getBookList(int userId);
    
    public Book getBook(int id);
    
    public void putBook(Book book);
    
    public void removeBook(int id);
    
    public void putBookList(int userId,List<Book> list);
    
    public void removeBookList(int userId);
    
    public Integer saveLibraryBook(LibraryBook libraryBook);
    
    public Page<BookVo> getLibraryBookList(int userId, String query,Page<BookVo> page);

    public List<BookVo> getBookList(String query);
    
    public UserBookLog saveUserBookLog(UserBookLog userBookLog);
    
    public boolean updateUserBookLog(UserBookLog userBookLog);
    
    public void removeUserBookLog(int id);
    
    public UserMessage saveUserMessage(UserMessage userMessage);
    
    public List<UserBookLog> getUserBookLogs(int userId);
    
    public UserBookLog getUserBookLog(int logId);
    
    public void putUserBookLog(UserBookLog userBookLog);
    
    public void removeUserBookLogs(int userId);
    
    public void putUserBookLogs(int userId,List<UserBookLog> list);
    
    public UserMessage getUserMessage(int id);
    
    public void putUserMessage(UserMessage userMessage);
    
    public List<UserMessage> getUserMessages(int userId);
    
    public void putUserLogMessages(int logId, List<UserMessage> list);
    
    public List<UserMessage> getUserLogMessages(int logId);
    
    public void removeUserMessages(int logId);
    
    public List<Library> getLibrarys(String query);
    
    public boolean deleteUserLibrary(int id, int userId);
    
    public boolean deleteLibrartBook(int libraryId,int userId);
     
    public int getLibraryBookCount(int libraryId);
    
    public int getLibraryUserCount(int libraryId);
    
    public void putLibraryUserCount(int libraryId,int count);
    
    public void putibraryBookCount(int libraryId,Integer count);
    
    public void removeLibraryBookCount(int libraryId);
    
    public int getBookCount();
    
    public void removeBookCount();
        
    public int getUserCount();
    
    public int getLibraryCount();
    
    public void putBookCount(Integer count);
    
    public boolean savePresentBookLog(PresentBookLog presentBookLog);
}
