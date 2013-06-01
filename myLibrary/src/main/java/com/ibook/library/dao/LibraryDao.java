package com.ibook.library.dao;

import java.util.List;
import java.util.Set;

import com.ibook.library.entity.Book;
import com.ibook.library.entity.BookTag;
import com.ibook.library.entity.Library;
import com.ibook.library.entity.LibraryBook;
import com.ibook.library.entity.PresentBookLog;
import com.ibook.library.entity.Tag;
import com.ibook.library.entity.UserBookLog;
import com.ibook.library.entity.UserInfo;
import com.ibook.library.entity.UserLibrary;
import com.ibook.library.entity.UserMessage;

public interface LibraryDao {

    public Integer saveBook(Book book);
    
    public boolean updateBook(Book book);
    
    public Book getBook(int id);
    
    public Integer saveUserBookLog(UserBookLog userBookLog);
    
    public boolean updateUserBookLog(UserBookLog userBookLog);
    
    public List<UserBookLog> getUserBookLogList(int ownerUserid);
    
    public UserBookLog getUserBookLog(int id);
    
    public Integer saveUserInfo(UserInfo userInfo);
    
    public UserInfo getUserInfo(int id);
    
    public UserInfo getUserInfo(String passport);
    
    public boolean updateUserInfo(UserInfo userInfo);
    
    public Integer saveUserMessage(UserMessage userMessage);
    
    public List<UserMessage> getUserMessage(int messageDirection,int userId);
    
    public List<UserMessage> getUserMessages(int logId) ;
    
    public UserMessage getUserMessage(int id) ;
    
    public boolean delUserMessage(int messageDirection,int userId);

    public boolean delUserMessage(int id);
      
    public UserLibrary getUserLibrary(int id);
    
    public UserLibrary getUserLibrary(int userId, int libraryId);
    
    public boolean delUserLibrary(int id);
    
    public Integer saveLibrary(Library library);
    
    public Library getLibrary(int id);
    
    public Library getLibrary(String name);
       
    public Integer saveUserLibrary(UserLibrary userLibrary);
    
    public List<UserLibrary> getUserLibraryList(int userId);
    
    public List<Book> getUserBookList(int userId);
    
    public Integer saveLibraryBook(LibraryBook libraryBook);
    
    public Set<Integer> getBookIdList(int userId, String query,int start,int end);
    
    public int getBookIdCount(int userId, String query);
    
    public Set<Integer> getBookIdList(String query,int start,int end);
    
    public int getBookIdCount(String query);
    
    public List<Library> getLibrarys(String query,String city);
    
    public boolean lockLibraryBook(int bookId);
    
    public boolean unLockLibraryBook(int bookId);
   
    public boolean delLibraryBook(int libraryId,int userId);
    
    public boolean delUserLibraryBook(int bookId,int userId);
    
    public int getLibraryBookCount(int libraryId);
    
    public int getBookCount();
    
    public int getUserCount();
    
    public int getLibraryCount();
    
    public int getLibraryUserCount(int libraryId);
    
    public boolean savePresentBookLog(PresentBookLog presentBookLog);
    
    public boolean saveTag(Tag tag);
    
    public boolean saveBookTag(BookTag bookTag);
        
}
